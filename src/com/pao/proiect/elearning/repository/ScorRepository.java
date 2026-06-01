package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.Scor;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScorRepository implements Repository<Scor, String> {

    @Override
    public void save(Scor entity) {
        String sql = """
                INSERT INTO scoruri (cursant_id, material_id, valoare)
                VALUES (?, ?, ?)
                """;
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, entity.getCursantId());
            ps.setInt(2, entity.getMaterialId());
            ps.setDouble(3, entity.getValoare());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea scorului", e);
        }
    }

    @Override
    public Optional<Scor> findById(String id) {
        String[] parts = id.split(":");
        return findByCursantAndMaterial(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    public Optional<Scor> findByCursantAndMaterial(int cursantId, int materialId) {
        String sql = """
                SELECT cursant_id, material_id, valoare
                FROM scoruri
                WHERE cursant_id = ? AND material_id = ?
                """;
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, cursantId);
            ps.setInt(2, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea scorului", e);
        }
    }

    public List<Scor> findByCursantId(int cursantId) {
        String sql = "SELECT cursant_id, material_id, valoare FROM scoruri WHERE cursant_id = ?";
        List<Scor> scoruri = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, cursantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    scoruri.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea scorurilor", e);
        }
        return scoruri;
    }

    public double calculeazaMediaLaCursCuJoin(int cursId, int cursantId) {
        String sql = """
                SELECT AVG(s.valoare) AS media
                FROM scoruri s
                JOIN materiale m ON s.material_id = m.id
                JOIN module mo ON m.modul_id = mo.id
                WHERE mo.curs_id = ? AND s.cursant_id = ? AND UPPER(m.tip) = 'QUIZ'
                """;
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, cursId);
            ps.setInt(2, cursantId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double media = rs.getDouble("media");
                    return rs.wasNull() ? 0.0 : media;
                }
                return 0.0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea JOIN scoruri-media", e);
        }
    }

    @Override
    public List<Scor> findAll() {
        String sql = "SELECT cursant_id, material_id, valoare FROM scoruri";
        List<Scor> scoruri = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                scoruri.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea scorurilor", e);
        }
        return scoruri;
    }

    @Override
    public void update(Scor entity) {
        String sql = """
                UPDATE scoruri
                SET valoare = ?
                WHERE cursant_id = ? AND material_id = ?
                """;
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setDouble(1, entity.getValoare());
            ps.setInt(2, entity.getCursantId());
            ps.setInt(3, entity.getMaterialId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea scorului", e);
        }
    }

    @Override
    public void delete(String id) {
        String[] parts = id.split(":");
        deleteByCursantAndMaterial(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    public void deleteByCursantAndMaterial(int cursantId, int materialId) {
        String sql = "DELETE FROM scoruri WHERE cursant_id = ? AND material_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, cursantId);
            ps.setInt(2, materialId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea scorului", e);
        }
    }

    private Scor mapRow(ResultSet rs) throws SQLException {
        return new Scor(
                rs.getInt("cursant_id"),
                rs.getInt("material_id"),
                rs.getDouble("valoare")
        );
    }
}
