package Training3;

import java.util.ResourceBundle;
/**
 * Fortuneインターフェース
 * @author e_kumakiri
 */
public interface Fortune {
	/** プロパティファイルの読み込み、値の取得(定数) */
	String DISP_STR = ResourceBundle.getBundle("fortune").getString("disp_str");

	/**
	 * 運勢を表示するための抽象メソッドです。
	 * 運勢結果の表示（抽象メソッド）
	 * @return　運勢の表示
	 */
	String disp();
}