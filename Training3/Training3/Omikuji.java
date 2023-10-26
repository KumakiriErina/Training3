package Training3;

/**
 * おみくじ抽象クラス
 * @author e_kumakiri
 */
public abstract class Omikuji implements Fortune {
	/** 運勢を表す */
	protected String unsei;
	/** 願い事を表す */
	protected String negaigoto;
	/** 商いを表す */
	protected String akinai;
	/** 学問を表す */
	protected String gakumon;

	/** 各運勢クラスで運勢をsetする */
	public abstract void setUnsei();
	/**
	 * 願い事をsetするためのメソッドです。
	 * @param negaigoto  願い事をsetする
	 */
	public void setNegaigoto(String negaigoto) {
		this.negaigoto = negaigoto;
	}
	/**
	 * 商いをsetするためのメソッドです。
	 * @param akinai 商いをsetする
	 */
	public void setAkinai(String akinai) {
		this.akinai = akinai;
	}
	/**
	 * 学問をsetするためのメソッドです。
	 * @param gakumon 学問をsetする
	 */
	public void setGakumon(String gakumon) {
		this.gakumon = gakumon;
	}
	/**
	 * 運勢を取得するためのメソッドです。
	 * @return 運勢
	 */
	public String getUnsei() {
		return this.unsei;
	}
	/**
	 * 願い事を取得するためのメソッドです。
	 * @return 願い事
	 */
	public String getNegaigoto() {
		return this.negaigoto;
	}
	/**
	 * 商いを取得するためのメソッドです。
	 * @return 商い
	 */
	public String getAkinai() {
		return this.akinai;
	}
	/**
	 * 学問を取得するためのメソッドです。
	 * @return 学問
	 */
	public String getGakumon() {
		return this.gakumon;
	}

	/**
	 * 運勢を表示するためのメソッドです。
	 * @return 運勢の表示
	 */
	public String disp() {
		String str = String.format(DISP_STR, getUnsei()) + System.lineSeparator() +"願い事:" +getNegaigoto() + System.lineSeparator() + "商い:" + getAkinai() + System.lineSeparator() + "学問:" + getGakumon();
		return str;
	}
}
