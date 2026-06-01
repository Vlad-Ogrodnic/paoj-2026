package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.Material;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaterialRepository implements Repository<Material, Integer> {

    public void save(int modulId, Material material) {
        String sql = "INSERT INTO materiale (id, modul_id, titlu, tip) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, material.getId());
            ps.setInt(2, modulId);
            ps.setString(3, material.getTitlu());
            ps.setString(4, material.getTip());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea materialului", e);
        }
    }

    @Override
    public void save(Material entity) {
        throw new UnsupportedOperationException("Foloseste save(modulId, material)");
    }

    @Override
    public Optional<Material> findById(Integer id) {
        String sql = "SELECT id, titlu, tip FROM materiale WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea materialului", e);
        }
    }

    public List<Material> findByModulId(int modulId) {
        String sql = "SELECT id, titlu, tip FROM materiale WHERE modul_id = ?";
        List<Material> materiale = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, modulId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    materiale.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea materialelor", e);
        }
        return materiale;
    }

    @Override
    public List<Material> findAll() {
        String sql = "SELECT id, titlu, tip FROM materiale";
        List<Material> materiale = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                materiale.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea materialelor", e);
        }
        return materiale;
    }

    @Override
    public void update(Material entity) {
        String sql = "UPDATE materiale SET titlu = ?, tip = ? WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, entity.getTitlu());
            ps.setString(2, entity.getTip());
            ps.setInt(3, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea materialului", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM materiale WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea materialului", e);
        }
    }

    private Material mapRow(ResultSet rs) throws SQLException {
        return new Material(rs.getInt("id"), rs.getString("titlu"), rs.getString("tip"));
    }
}
