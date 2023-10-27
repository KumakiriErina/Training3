package Training3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * DB接続用のクラスです
 * @author e_kumakiri
 *
 */
public class DBManager {
	/** ドライバクラス名 */
	private static final String DRIVER = "org.postgresql.Driver";
	/** 接続するDBのURL */
	private static final String URL = "jdbc:postgresql://localhost:5432/banana";
	/** DB接続するためのユーザ名 */
	private static final String USER_NAME = "postgres";
	/** DB接続するためのパスワード */
	private static final String PASSWORD = "kumakiri2005";
	
	/**
	 * DBと接続するメソッドです
	 * 
	 * @return connection DBコネクション
	 * @exception  ClassNotFoundException ドライバクラスが見つからなかった場合
	 * @exception SQLException DB接続に失敗した場合
	 */
	public static Connection getConnection() throws ClassNotFoundException, SQLException{
		//JDBCドライバクラスをJVMに登録
		Class.forName(DRIVER);

		//DBに接続
		Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
		System.out.println("DBに接続しました");
		return connection;
	}
	/**
	 * DBとの接続を切断するメソッドです
	 * 
	 * @param connection DBとの接続情報
	 */
	public static void close(Connection connection) {
		if(connection != null) {
			//nullでなかったら閉じる
			try {
				connection.close();
				System.out.println("DBと切断しました");
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * PreparedStatementをクローズするメソッドです
	 * 
	 * @param preparedStatement ステートメント情報
	 */
	public static void close(PreparedStatement preparedStatement) {
		if(preparedStatement != null) {
			//nullでなかったら閉じる
			try {
				preparedStatement.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * ResultSetをクローズするメソッドです
	 * 
	 * @param resultSet SQL検索結果
	 */
	public static void close(ResultSet resultSet) {
		if(resultSet != null) {
			//nullでなかったら閉じる
			try {
				resultSet.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
