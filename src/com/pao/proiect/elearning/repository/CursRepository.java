package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.Curs;
import com.pao.proiect.elearning.model.CursCuProfesor;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CursRepository implements Repository<Curs, Integer> {

    private final ModulRepository modulRepository = new ModulRepository();

    @Override
    public void save(Curs entity) {
        String sql = "INSERT INTO cursuri (id, titlu, profesor_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getTitlu());
            ps.setInt(3, entity.getProfesorId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea cursului", e);
        }
    }

    @Override
    public Optional<Curs> findById(Integer id) {
        String sql = "SELECT id, titlu, profesor_id FROM cursuri WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Curs curs = mapRow(rs);
                    modulRepository.findByCursId(curs.getId()).forEach(curs::adaugaModul);
                    return Optional.of(curs);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea cursului", e);
        }
    }

    @Override
    public List<Curs> findAll() {
        String sql = "SELECT id, titlu, profesor_id FROM cursuri";
        List<Curs> cursuri = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Curs curs = mapRow(rs);
                modulRepository.findByCursId(curs.getId()).forEach(curs::adaugaModul);
                cursuri.add(curs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea cursurilor", e);
        }
        return cursuri;
    }

    @Override
    public void update(Curs entity) {
        String sql = "UPDATE cursuri SET titlu = ?, profesor_id = ? WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, entity.getTitlu());
            ps.setInt(2, entity.getProfesorId());
            ps.setInt(3, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea cursului", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM cursuri WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea cursului", e);
        }
    }

    public void deleteCuDependinte(int cursId) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        try {
            deleteScoruriPentruCurs(conn, cursId);
            deleteMaterialePentruCurs(conn, cursId);
            deleteModulePentruCurs(conn, cursId);
            deleteInscrieriPentruCurs(conn, cursId);
            deleteCurs(conn, cursId);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private void deleteScoruriPentruCurs(Connection conn, int cursId) throws SQLException {
        String sql = """
                DELETE FROM scoruri
                WHERE material_id IN (
                    SELECT m.id FROM materiale m
                    JOIN module mo ON m.modul_id = mo.id
                    WHERE mo.curs_id = ?
                )
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cursId);
            ps.executeUpdate();
        }
    }

    private void deleteMaterialePentruCurs(Connection conn, int cursId) throws SQLException {
        String sql = """
                DELETE FROM materiale
                WHERE modul_id IN (SELECT id FROM module WHERE curs_id = ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cursId);
            ps.executeUpdate();
        }
    }

    private void deleteModulePentruCurs(Connection conn, int cursId) throws SQLException {
        String sql = "DELETE FROM module WHERE curs_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cursId);
            ps.executeUpdate();
        }
    }

    private void deleteInscrieriPentruCurs(Connection conn, int cursId) throws SQLException {
        String sql = "DELETE FROM inscrieri WHERE curs_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cursId);
            ps.executeUpdate();
        }
    }

    private void deleteCurs(Connection conn, int cursId) throws SQLException {
        String sql = "DELETE FROM cursuri WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cursId);
            ps.executeUpdate();
        }
    }

    public List<Curs> findByProfesorId(int profesorId) {
        String sql = "SELECT id, titlu, profesor_id FROM cursuri WHERE profesor_id = ?";
        List<Curs> cursuri = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, profesorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Curs curs = mapRow(rs);
                    modulRepository.findByCursId(curs.getId()).forEach(curs::adaugaModul);
                    cursuri.add(curs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea cursurilor profesorului", e);
        }
        return cursuri;
    }

    public List<CursCuProfesor> findCursuriCuProfesorJoin(int profesorId) {
        String sql = """
                SELECT c.id, c.titlu, u.nume AS nume_profesor
                FROM cursuri c
                JOIN utilizatori u ON c.profesor_id = u.id
                WHERE c.profesor_id = ?
                ORDER BY c.titlu
                """;
        List<CursCuProfesor> rezultat = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, profesorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rezultat.add(new CursCuProfesor(
                            rs.getInt("id"),
                            rs.getString("titlu"),
                            rs.getString("nume_profesor")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea JOIN cursuri-profesor", e);
        }
        return rezultat;
    }

    private Curs mapRow(ResultSet rs) throws SQLException {
        return new Curs(rs.getInt("id"), rs.getString("titlu"), rs.getInt("profesor_id"));
    }
}
