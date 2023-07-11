package Sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

class Account {
	int accNo;
	String type;
	String opdate;
	double bal;

	public Account(int accNo, String type, String opdate, double bal) {
		this.accNo = accNo;
		this.type = type;
		this.opdate = opdate;
		this.bal = bal;
	}

	public int getAccNo() {
		return accNo;
	}

	public void setAccNo(int accNo) {
		this.accNo = accNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOpdate() {
		return opdate;
	}

	public void setOpdate(String opdate) {
		this.opdate = opdate;
	}

	public double getBal() {
		return bal;
	}

	public void setBal(double bal) {
		this.bal = bal;
	}
}

class Transaction {
	int tid;
	int accNo;
	String tdate;
	String type;
	double amt;

	public Transaction(int tid, int accNo, String tdate, String type, double amt) {
		super();
		this.tid = tid;
		this.accNo = accNo;
		this.tdate = tdate;
		this.type = type;
		this.amt = amt;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public int getAccNo() {
		return accNo;
	}

	public void setAccNo(int accNo) {
		this.accNo = accNo;
	}

	public String getTdate() {
		return tdate;
	}

	public void setTdate(String tdate) {
		this.tdate = tdate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmt() {
		return amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}
}

class Caluculation {
	ArrayList<Account> arr1 = new ArrayList<>();
	ArrayList<Transaction> arr2 = new ArrayList<>();

	public void loadDetails() throws IOException {
		File f1 = new File("C:\\Users\\satish.ki\\Documents\\satish\\act.txt");
		File f2 = new File("C:\\Users\\satish.ki\\Documents\\satish\\transaction.txt");
		FileReader fr1 = new FileReader(f1);
		BufferedReader br1 = new BufferedReader(fr1);
		String str;
		while ((str = br1.readLine()) != null) {
			Account act = new Account(0, "", "", 0);
			StringTokenizer st = new StringTokenizer(str, ",");
			if (st.hasMoreTokens()) {
				act.setAccNo(Integer.parseInt(st.nextToken().trim()));
			}
			if (st.hasMoreTokens()) {
				act.setType(st.nextToken().trim());
			}
			if (st.hasMoreTokens()) {
				act.setOpdate(st.nextToken().trim());
			}
			if (st.hasMoreTokens()) {
				act.setBal(Double.parseDouble(st.nextToken().trim()));
			}
			arr1.add(act);
		}
		FileReader fr2 = new FileReader(f2);
		BufferedReader br2 = new BufferedReader(fr2);
		String str1;
		while ((str1 = br2.readLine()) != null) {
			Transaction tcn = new Transaction(0, 0, "", "", 0);
			StringTokenizer st = new StringTokenizer(str1, ",");
			if (st.hasMoreTokens()) {
				tcn.setTid(Integer.parseInt(st.nextToken().trim()));
			}
			if (st.hasMoreTokens()) {
				tcn.setAccNo(Integer.parseInt(st.nextToken().trim()));
			}
			if (st.hasMoreTokens()) {
				tcn.setTdate(st.nextToken().trim());
			}
			if (st.hasMoreTokens()) {
				tcn.setType(st.nextToken().trim());
			}
			if (st.hasMoreTokens()) {
				tcn.setAmt(Double.parseDouble(st.nextToken().trim()));
			}
			arr2.add(tcn);
		}
	}

	public ArrayList<Transaction> getTransactions(int ano, String date1, String date2) throws Exception {
		ArrayList<Transaction> arr3 = new ArrayList<>();
		SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
		Date d1 = f.parse(date1);
		Date d2 = f.parse(date2);
		for (Transaction tr : arr2) {
			Date d = f.parse(tr.getTdate());
			if (tr.getAccNo() == ano) {
				if (d.after(d1) && d.before(d2)) {
					arr3.add(tr);
				}
			}
		}
		return arr3;
	}

	public double getBalanceAsOndate(int accno, String date) throws Exception {
		Date date1 = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
		String dat = df.format(date1);
		Account b = null;
		for (Account a : arr1) {
			if (a.getAccNo() == accno) {
				b = a;
				break;
			}
		}
		if (b == null) {
			return 0;
		}
		double ba = b.getBal();
		ArrayList<Transaction> arr4 = getTransactions(accno, date, dat);
		for (int i = arr4.size() - 1; i >= 0; i--) {
			Transaction t = arr4.get(i);
			if (t.getType().equals("D")) {
				ba = ba - t.getAmt();
			} else {
				ba = ba + t.getAmt();
			}
		}
		return ba;
	}

	public double getMinBal(int acc, String m) throws Exception {
		String s = "10-" + m + "-2023";
		double b = getBalanceAsOndate(acc, s);
		for (int i = 11; i < 31; i++) {
			String s1 = "10-" + m + "-2023";
			double b1 = getBalanceAsOndate(acc, s1);
			if (b1 < b) {
				b = b1;
			}
		}
		return b;
	}

	public void calInterst(Account a) throws Exception {
		double interest = 0;
		String cdate = a.getOpdate();
		Date date1 = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
		String pdate = df.format(date1);
		int m = date1.getMonth();
		int d = date1.getDay();
		int y = date1.getYear();
		String dd = String.valueOf(d);
		String yy = String.valueOf(y);
		String idate = dd + "-" + (m - 6) + "-" + yy;
		if (cdate.compareTo(idate) < 0) {
			for (int i = 0; i < 6; i++) {
				String ss = dd + "-" + (m - i) + "-" + yy;
				double ba = getMinBal(a.getAccNo(), ss);
				double in;
				in = (ba * 0.45) / 100;
				interest += in;
			}
			System.out.println(interest);
		}
	}

	public void A() throws Exception {
		System.out.println(getBalanceAsOndate(1001, "05-06-2023"));
		System.out.println(getMinBal(1001, "06"));
		calInterst(arr1.get(0));
	}
}

public class BankAccountInt {
	public static void main(String args[]) throws Exception {
		Caluculation c = new Caluculation();
		c.loadDetails();
		c.A();
	}
}