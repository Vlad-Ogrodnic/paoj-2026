package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.Cursant;
import com.pao.proiect.elearning.model.Inscriere;
import com.pao.proiect.elearning.model.Profesor;
import com.pao.proiect.elearning.model.Utilizator;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InscriereRepository implements Repository<Inscriere, String> {

    @Override
    public void save(Inscriere entity) {
        String sql = """
                INSERT INTO inscrieri (curs_id, cursant_id, data_inscrierii)
                VALUES (?, ?, ?)
                """;
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, entity.getCursId());
            ps.setInt(2, entity.getCursantId());
            ps.setString(3, entity.getDataInscrierii().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea inscrierii", e);
        }
    }

    @Override
    public Optional<Inscriere> findById(String id) {
        String[] parts = id.split(":");
        return findByCursAndCursant(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    public Optional<Inscriere> findByCursAndCursant(int cursId, int cursantId) {
        String sql = """
                SELECT curs_id, cursant_id, data_inscrierii
                FROM inscrieri
                WHERE curs_id = ? AND cursant_id = ?
                """;
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, cursId);
            ps.setInt(2, cursantId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea inscrierii", e);
        }
    }

    public List<Inscriere> findByCursId(int cursId) {
        String sql = "SELECT curs_id, cursant_id, data_inscrierii FROM inscrieri WHERE curs_id = ?";
        List<Inscriere> inscrieri = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, cursId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inscrieri.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea inscrierilor", e);
        }
        return inscrieri;
    }

    public boolean exists(int cursId, int cursantId) {
        return findByCursAndCursant(cursId, cursantId).isPresent();
    }

    public List<Utilizator> findCursantiInscrisiCuJoin(int cursId) {
        String sql = """
                SELECT u.id, u.nume, u.email, u.rol, u.departament, u.an_studiu
                FROM inscrieri i
                JOIN utilizatori u ON i.cursant_id = u.id
                WHERE i.curs_id = ? AND u.rol = 'CURSANT'
                ORDER BY u.nume
                """;
        List<Utilizator> cursanti = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, cursId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cursanti.add(mapUtilizatorRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea JOIN inscrieri-cursanti", e);
        }
        return cursanti;
    }

    @Override
    public List<Inscriere> findAll() {
        String sql = "SELECT curs_id, cursant_id, data_inscrierii FROM inscrieri";
        List<Inscriere> inscrieri = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                inscrieri.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea inscrierilor", e);
        }
        return inscrieri;
    }

    @Override
    public void update(Inscriere entity) {
        String sql = """
                UPDATE inscrieri
                SET data_inscrierii = ?
                WHERE curs_id = ? AND cursant_id = ?
                """;
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, entity.getDataInscrierii().toString());
            ps.setInt(2, entity.getCursId());
            ps.setInt(3, entity.getCursantId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea inscrierii", e);
        }
    }

    @Override
    public void delete(String id) {
        String[] parts = id.split(":");
        deleteByCursAndCursant(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    public void deleteByCursAndCursant(int cursId, int cursantId) {
        String sql = "DELETE FROM inscrieri WHERE curs_id = ? AND cursant_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, cursId);
            ps.setInt(2, cursantId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea inscrierii", e);
        }
    }

    private Inscriere mapRow(ResultSet rs) throws SQLException {
        return new Inscriere(
                rs.getInt("curs_id"),
                rs.getInt("cursant_id"),
                LocalDateTime.parse(rs.getString("data_inscrierii"))
        );
    }

    private Utilizator mapUtilizatorRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nume = rs.getString("nume");
        String email = rs.getString("email");
        String rol = rs.getString("rol");
        if ("PROFESOR".equals(rol)) {
            return new Profesor(id, nume, email, rs.getString("departament"));
        }
        return new Cursant(id, nume, email, rs.getInt("an_studiu"));
    }
}
