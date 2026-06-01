package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.Cursant;
import com.pao.proiect.elearning.model.Profesor;
import com.pao.proiect.elearning.model.Utilizator;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilizatorRepository implements Repository<Utilizator, Integer> {

    @Override
    public void save(Utilizator entity) {
        String sql = """
                INSERT INTO utilizatori (id, nume, email, rol, departament, an_studiu)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getNume());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getRol());
            if (entity instanceof Profesor profesor) {
                ps.setString(5, profesor.getDepartament());
                ps.setNull(6, java.sql.Types.INTEGER);
            } else if (entity instanceof Cursant cursant) {
                ps.setNull(5, java.sql.Types.VARCHAR);
                ps.setInt(6, cursant.getAnStudiu());
            } else {
                ps.setNull(5, java.sql.Types.VARCHAR);
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea utilizatorului", e);
        }
    }

    @Override
    public Optional<Utilizator> findById(Integer id) {
        String sql = "SELECT id, nume, email, rol, departament, an_studiu FROM utilizatori WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea utilizatorului", e);
        }
    }

    @Override
    public List<Utilizator> findAll() {
        String sql = "SELECT id, nume, email, rol, departament, an_studiu FROM utilizatori";
        List<Utilizator> utilizatori = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                utilizatori.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea utilizatorilor", e);
        }
        return utilizatori;
    }

    @Override
    public void update(Utilizator entity) {
        String sql = """
                UPDATE utilizatori
                SET nume = ?, email = ?, rol = ?, departament = ?, an_studiu = ?
                WHERE id = ?
                """;
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getRol());
            if (entity instanceof Profesor profesor) {
                ps.setString(4, profesor.getDepartament());
                ps.setNull(5, java.sql.Types.INTEGER);
            } else if (entity instanceof Cursant cursant) {
                ps.setNull(4, java.sql.Types.VARCHAR);
                ps.setInt(5, cursant.getAnStudiu());
            } else {
                ps.setNull(4, java.sql.Types.VARCHAR);
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setInt(6, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea utilizatorului", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM utilizatori WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea utilizatorului", e);
        }
    }

    private Utilizator mapRow(ResultSet rs) throws SQLException {
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
