package Training3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 実行をするクラスです
 * @author e_kumakiri
 * @version 1.0
 *
 */
public class Training3 {
	/**
	 * メインメソッド
	 * @param args 引数　使用しない
	 */
	public static void main(String[] args) {
		//パスの取得
		Path path = Paths.get("/Users/e_kumakiri/Desktop/workspace/Training3/src/Fortune.csv");

		//BufferedReaderクラスを宣言
		BufferedReader reader = null;

		//日付入力のフォーマットの宣言
		SimpleDateFormat simpleDateFormat = null;

		//ランダムクラスの準備
		Random rand = null;
		
		//現在の日付を準備
		Date date = null;

		//readLine()メソッドを使って入力した1行データを読み込むための準備
		String inputStr = null;

		//入力したデータをDate型に変換する準備
		Date inputDate = null;

		//おみくじオブジェクトの宣言
		Omikuji omikuji = null;
		
		//おみくじコードの最大件数を取得するための宣言
		int countOmikujiCode = 0;

		//DBに接続するために宣言
		Connection connection = null;

		//PreparedStatemenrの準備
		PreparedStatement preparedStatement = null;

		//ResultSetの準備
		ResultSet resultSet = null;

		try {
			//csvファイルの読み込み
			List<String> line = Files.readAllLines(path);

			//箱を使えるように、目の前に準備
			//DBに接続
			connection = DBManager.getConnection();
			
			//最大件数を出したいSQL(INSERTの条件、ランダムの最大件数に使用)
			//AS countOmikuji 別名をつけている
			String countOmikuji = "SELECT COUNT(omikuji_code) AS countOmikuji FROM Omikuji;";

			//ステートメント（SQL文を受け取って実行）
			Statement statement1 = connection.createStatement();
			
			//SQL文を実行して、その結果をresultSetに代入
			resultSet = statement1.executeQuery(countOmikuji);


			if (resultSet.next()) {
				//1件取得(最大件数)
				countOmikujiCode = resultSet.getInt("countOmikuji");
			}

			//最大件数が50未満だったら登録処理にすすむ
			if(countOmikujiCode < 50) {

				//50回分する
				for (int i = 0; i < line.size(); i++) {

					//dataの,を取り除く
					String[] data = line.get(i).split(",");

					//SQL文を準備(OmikujiテーブルをINSERTする)
					String sqlInsertOmikuji = "INSERT INTO Omikuji VALUES(?, ?, ?, ?, ?, 'kumakiri', CURRENT_DATE, 'kumakiri', CURRENT_DATE);";

					//ステートメントの作成（オブジェクト生成）
					preparedStatement = connection.prepareStatement(sqlInsertOmikuji);

					//SQL中の各プレースホルダーに入力値をバインド
					preparedStatement.setString(1, data[0]);
					//各具象クラスの戻り値をバインド
					preparedStatement.setString(2, convertUnsei(data[1]));
					preparedStatement.setString(3, data[2]);
					preparedStatement.setString(4, data[3]);
					preparedStatement.setString(5, data[4]);

					//SQL文を実行(登録の際はUpdate)
					int num = preparedStatement.executeUpdate();

					//登録された件数が1件でなければ出力
					if(num != 1) {
						System.out.println("登録されていません");
					}

					//close処理
					preparedStatement.close();
				}
			}

			//入力された値が正しくない間実行するための条件
			while (true) {
				try {
					//System.inからInputStreamReaderクラスのオブジェクト作成
					//BufferedReaderクラスのオブジェクト作成
					reader = new BufferedReader(new InputStreamReader(System.in));

					//誕生日の入力
					System.out.println("誕生日を入力してください(例:20150809)");

					//日付入力のフォーマット（yyyyMMdd）の生成
					simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

					//入力された値が正しい（存在している）かチェック
					simpleDateFormat.setLenient(false);

					//readLine()メソッドを使って入力した1行データを読み込む
					inputStr = reader.readLine();
					
					if(!inputStr.matches("^[0-9]{4}[0-9]{2}[0-9]{2}$")) {
						System.out.println("形式がyyyyMMddになっていないです");
						continue;
					}
					//入力したデータをDate型に変換
					inputDate = simpleDateFormat.parse(inputStr);
					
					//入力した日付をフォーマット
					inputStr = simpleDateFormat.format(inputDate);
	
				} catch (ParseException pe) {
					//入力した日付が存在しないか、フォーマットが違う場合
					System.out.println("存在しない日付です");
					continue;
				}catch(NumberFormatException ne) {
					System.out.println("数値以外が入力されています");
					continue;
				}
				//正しい値が入力されたら抜ける
				break;
			}

			//Date型（現在）の生成
			date = new Date();

			//Date型(現在)をString型に変換
			String now = new SimpleDateFormat("yyyyMMdd").format(date);

			//ランダムオブジェクトを生成する（本日と入力した値のString型をInteger型に変換）
			rand = new Random(Integer.parseInt(now) + Integer.parseInt(inputStr));

			//おみくじをランダムにするための準備(おみくじコードの最大値を上限とする)
			//おみくじコードを取得するためのSQL文(138行目で値をバインド)
			String result = "SELECT * FROM Omikuji WHERE omikuji_code = ? ";

			//ステートメント作成（オブジェクト生成）
			PreparedStatement preparedStatement2 = connection.prepareStatement(result);

			/** ランダムの最小値を定義しています*/
			final int one = 1;

			//ランダムにしたおみくじの値をバインド(116行目のSQL文)
			//omikuji_codeの?の部分に、同じ日に同じ運勢が返ってくる + 50個(omikuji_codeの最大値)分ランダムにしている
			//rand.nextIntをString型に変換
			preparedStatement2.setString(1, String.valueOf(rand.nextInt(countOmikujiCode) + one));

			//SQLを実行(preparedStatement2のオブジェクトが代入される)
			ResultSet resultSet3 = preparedStatement2.executeQuery();

			String omikujiCode = "";

			//next：次があるか、カーソル の役割
			if (resultSet3.next()) {
				//おみくじの情報を取得
				omikujiCode = resultSet3.getString("omikuji_code");
				String unseiCode = resultSet3.getString("unsei_code");
				String negaigoto = resultSet3.getString("negaigoto");
				String akinai = resultSet3.getString("akinai");
				String gakumon = resultSet3.getString("gakumon");

				//オブジェクト生成
				omikuji = getOmikuji(unseiCode);

				//値をsetする
				omikuji.setUnsei();
				omikuji.setNegaigoto(negaigoto);
				omikuji.setAkinai(akinai);
				omikuji.setGakumon(gakumon);
				}
				//close処理
				preparedStatement2.close();
				//おみくじの内容をコンソールに表示
				System.out.println(omikuji.disp());

				//結果テーブルにおみくじの内容を登録(resultテーブルにINSERTする)
				String sqlInsertResult = "INSERT INTO result VALUES(?, ?, ?, 'kumakiri', CURRENT_DATE, 'kumakiri', CURRENT_DATE)";

				//ステートメントの作成
				PreparedStatement preparedStatement3 = connection.prepareStatement(sqlInsertResult);

				//占った日をjava.util.Dateから、java.sql.Dateへ変換
				java.sql.Date dateConvertDate = new java.sql.Date(date.getTime());

				//誕生日をjava.util.Dateから、java.sql.Dateへ変換
				java.sql.Date dateConvertInputDate = new java.sql.Date(inputDate.getTime());

				//SQL中の各プレースホルダーに入力値をバインド
				preparedStatement3.setDate(1, dateConvertDate); //占った日
				preparedStatement3.setDate(2, dateConvertInputDate); //誕生日
				preparedStatement3.setString(3, omikujiCode);//おみくじコードの取得

				//SQL文を実行(登録の際はUpdate)
				preparedStatement3.executeUpdate();
				
				//close処理
				preparedStatement3.close();

		} catch (IOException ie) {
			//BufferedReaderの処理に失敗した場合
			System.out.println("BufferedReader関係でエラーです");
			ie.printStackTrace();

		} catch (SQLException se) {
			//DB接続関連でのエラー
			System.out.println("DB関係でエラーです");
			se.printStackTrace();

		} catch (ClassNotFoundException ce) {
			//クラスが見つからなかった時のエラー
			System.out.println("クラスが見つかりません");
			ce.printStackTrace();

		} finally {
			//クローズ処理
			//DBとの接続を切断
			DBManager.close(connection);
		}
	}

	/**
	 * 運勢名によって返す値が変化するメソッドです
	 * 
	 * @param unseiName 運勢名
	 * @return　各具象クラスの番号
	 */
	private static String convertUnsei(String unseiName) {
		//data[1]の運勢名によって返す値が変化する
		switch (unseiName) {
		case "大吉":
			//大吉だったら01を返す
			return "01";

		case "中吉":
			return "02";

		case "小吉":
			return "03";

		case "末吉":
			return "04";

		case "吉":
			return "05";

		case "凶":
			return "06";

		default:
			//01から06以外だったら例外を投げる
			throw new IllegalArgumentException("予想外の値です");
		}
	}

	/**
	 * 運勢コードを元にオブジェクト生成をするクラスです
	 * 
	 * @param unseiCode 運勢コード
	 * @return おみくじクラス
	 */
	private static Omikuji getOmikuji(String unseiCode) {

		//取得したおみくじ情報を元にオブジェクト生成
		switch (unseiCode) {
		case "01":
			//01だったら大吉オブジェクトを生成
			return new GreatBlessing();

		case "02":
			return new MiddleBlassing();

		case "03":
			return new SmallBlessing();

		case "04":
			return new UncertinLuck();

		case "05":
			return new GreatBlessing();

		case "06":
			return new BadLuck();

		default:
			//01から06以外だったら例外を投げる
			throw new IllegalArgumentException("予想外の値です");
		}
	}
}
