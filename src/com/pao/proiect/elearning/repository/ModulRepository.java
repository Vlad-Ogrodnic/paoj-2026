package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.Modul;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModulRepository implements Repository<Modul, Integer> {

    private final MaterialRepository materialRepository = new MaterialRepository();

    public void save(int cursId, Modul modul) {
        String sql = "INSERT INTO module (id, curs_id, titlu) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, modul.getId());
            ps.setInt(2, cursId);
            ps.setString(3, modul.getTitlu());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea modulului", e);
        }
    }

    @Override
    public void save(Modul entity) {
        throw new UnsupportedOperationException("Foloseste save(cursId, modul)");
    }

    @Override
    public Optional<Modul> findById(Integer id) {
        String sql = "SELECT id, titlu FROM module WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Modul modul = new Modul(rs.getInt("id"), rs.getString("titlu"));
                    materialRepository.findByModulId(modul.getId()).forEach(modul::adaugaMaterial);
                    return Optional.of(modul);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea modulului", e);
        }
    }

    public List<Modul> findByCursId(int cursId) {
        String sql = "SELECT id, titlu FROM module WHERE curs_id = ?";
        List<Modul> module = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, cursId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Modul modul = new Modul(rs.getInt("id"), rs.getString("titlu"));
                    materialRepository.findByModulId(modul.getId()).forEach(modul::adaugaMaterial);
                    module.add(modul);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea modulelor", e);
        }
        return module;
    }

    @Override
    public List<Modul> findAll() {
        String sql = "SELECT id, titlu FROM module";
        List<Modul> module = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Modul modul = new Modul(rs.getInt("id"), rs.getString("titlu"));
                materialRepository.findByModulId(modul.getId()).forEach(modul::adaugaMaterial);
                module.add(modul);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea modulelor", e);
        }
        return module;
    }

    @Override
    public void update(Modul entity) {
        String sql = "UPDATE module SET titlu = ? WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, entity.getTitlu());
            ps.setInt(2, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea modulului", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM module WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea modulului", e);
        }
    }
}
