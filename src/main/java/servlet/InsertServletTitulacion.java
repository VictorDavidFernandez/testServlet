package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InsertServletTitulacion extends HttpServlet {
	private String jdbcUrl = "jdbc:h2:file:~/testdb";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String titulo = req.getParameter("titulo");

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
			preparedStatement = conn.prepareStatement("create table IF NOT EXISTS TITULACION(\r\n"
					+ "id bigint auto_increment,\r\n" + "titulo varchar(25)\r\n" + ");");
			preparedStatement.executeUpdate();
			preparedStatement.close();
			preparedStatement = conn.prepareStatement("INSERT INTO TITULACION (titulo) VALUES (?)");
			preparedStatement.setString(1, titulo);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			preparedStatement = conn.prepareStatement("SELECT id, titulo FROM TITULACION");
			ResultSet resultSet = preparedStatement.executeQuery();
			ArrayList<String> listNombresTitulaciones = new ArrayList<String>();
			ArrayList<Integer> listTitulacionesId = new ArrayList<Integer>();
			while (resultSet.next()) {
				Integer integer = resultSet.getInt(1);
				listTitulacionesId.add(integer);
				String string = resultSet.getString(2);
				listNombresTitulaciones.add(string);
			}
			req.setAttribute("titulacionesId", listTitulacionesId);
			req.setAttribute("nombresTitulaciones", listNombresTitulaciones);
			preparedStatement.close();
			conn.close();
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
