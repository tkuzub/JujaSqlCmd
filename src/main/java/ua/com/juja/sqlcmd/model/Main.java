package ua.com.juja.sqlcmd.model;

import java.sql.*;

public class Main {
	public static final String USER_NAME = "postgres";
	public static final String DATABASE_NAME = "jdbc:postgresql://localhost/sqlcmd_db";
	public static final String PASSWORD = "777";

	public static void main(String[] args) throws SQLException {
		Connection connection = DriverManager.getConnection(DATABASE_NAME, USER_NAME, PASSWORD);

		//Insert
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("INSERT INTO public.user_info(NAME, PASSWORD) "
				+ "VALUES ('Stiven Pupkin', 'pass' );");
		stmt.close();

		//delete
		stmt = connection.createStatement();
		stmt.executeUpdate("DELETE from public.user_info WHERE id > 10;");
		stmt.close();

		//select
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM public.user_info;");
		while (rs.next()) {
			System.out.println("id: " + rs.getString(1));
			System.out.println("name: " + rs.getString(2));
			System.out.println("password: " + rs.getString(3));
			System.out.println("=============");
		}
		rs.close();

		//update
		PreparedStatement pstmt = connection.prepareStatement("UPDATE public.user_info "
				+ "SET password = ? "
				+ "WHERE id > 5;");

			pstmt.setString(1, "passsss");
			pstmt.executeUpdate();
			pstmt.close();

		connection.close();
	}
}
