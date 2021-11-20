package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InsertServletAlumno extends HttpServlet {
	private String jdbcUrl = "jdbc:h2:file:~/testdb";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int titulacion = Integer.parseInt(req.getParameter("titulacionId"));
		String nombre = req.getParameter("nombre");
		int edad = Integer.parseInt(req.getParameter("edad"));

		Connection conn;
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection(jdbcUrl, "sa", "");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement("create table IF NOT EXISTS ALUMNO(\r\n"
					+ "id bigint auto_increment,\r\n" + "nombre varchar(25),\r\n" + "edad bigint,\r\n"
					+ "titulacion INT,\r\n" + "FOREIGN KEY (titulacion) REFERENCES TITULACION(id)\r\n" + ");");
			preparedStatement.executeUpdate();
			preparedStatement.close();
			preparedStatement = conn.prepareStatement("INSERT INTO ALUMNO (nombre, edad, titulacion) VALUES (?, ?, ?)");
			preparedStatement.setString(1, nombre);
			preparedStatement.setInt(2, edad);
			preparedStatement.setInt(3, titulacion);
			preparedStatement.executeUpdate();
			preparedStatement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/alumno.jsp");
		dispatcher.forward(req, resp);
	}
}
