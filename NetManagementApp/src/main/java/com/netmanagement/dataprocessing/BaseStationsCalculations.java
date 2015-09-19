package com.netmanagement.dataprocessing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.netmanagement.csvdatasets.ParseBaseStations;
import com.netmanagement.entities.BaseStations;

public class BaseStationsCalculations {

	private static BaseStationsCalculations BaseStationsCalculationsinstance = null;

	private BaseStationsCalculations() {
	}

	public static BaseStationsCalculations getInstance() {
		if (BaseStationsCalculationsinstance == null) {
			BaseStationsCalculationsinstance = new BaseStationsCalculations();
		}
		return BaseStationsCalculationsinstance;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<BaseStations> searchUser(String userID, Date startDate,
			Date endDate) {
		// Search data for a specific user with the date contained between
		// startDate and endDate. Then return a list with these data.
		System.out.println("userID: " + userID + " startDate: " + startDate
				+ " endDate: " + endDate);
		HashMap<String, ArrayList<BaseStations>> hap = ParseBaseStations
				.getInstance().getHap();
		ArrayList<BaseStations> alist = new ArrayList<BaseStations>();
		if (!hap.isEmpty()) {
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				ArrayList<BaseStations> array = (ArrayList<BaseStations>) me.getValue();
				for (int i = 0; i < array.size(); i++) {
					BaseStations tempap = array.get(i);
					if (tempap.getUser().equals(userID)) {
						
						if (startDate.equals(tempap.getTimestamp()) || startDate.before(tempap.getTimestamp())) {
							if (endDate.equals(tempap.getTimestamp()) || endDate.after(tempap.getTimestamp())) {
								if (!tempap.getBSlatitude().equals("No Latitude")|| !tempap.getBSlongtitude().equals("No longitude")) {
									alist.add(tempap);
								}
								
							}
						}

					}
				}
			}
		}
		if (alist.isEmpty()) {
			System.out.println("BaseStationsCalculations: alist is empty!!!");
		}
		return alist;
	}

	class Operator {
		String Name;
		int numofUsers = 0;
		int mcc;
		int mnc;
		ArrayList<String> users = new ArrayList<String>();

		void setAll(String operator, int i, int mcc, int mnc, String user) {
			System.out.println(Name + " | " + numofUsers + " | " + mcc + " | "
					+ mnc + " | " + users);
			this.Name = operator;
			this.numofUsers = i;
			this.mcc = mcc;
			this.mnc = mnc;
			this.users.add(user);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<String> Operators_numofUsers() {
		// Find the name of each operator and the number of his unique users.
		// Return an array list of operator_name#number_of_users
		HashMap<String, ArrayList<BaseStations>> hap = ParseBaseStations
				.getInstance().getHap();
		ArrayList<Operator> operators = new ArrayList<Operator>();
		if (!hap.isEmpty()) {
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				
				ArrayList<BaseStations> array = (ArrayList<BaseStations>) me.getValue();
				for (int i = 0; i < array.size(); i++) {
					if (operators.size() == 0) {
						Operator temp = new Operator();
						temp.setAll(array.get(i).getOperator(),
								temp.numofUsers + 1, array.get(i).getMcc(),
								array.get(i).getMnc(), array.get(i).getUser());
						operators.add(temp);
						continue;
					}
					int found = 0;
					int j = 0;
					for (j = 0; j < operators.size(); j++) {
						if ((operators.get(j).mcc == array.get(i).getMcc())
								&& (operators.get(j).mnc == array.get(i)
										.getMnc())) {
							if (!operators.get(j).users.contains(array.get(i)
									.getUser())) {
								// operators.get(j).setAll(array.get(i).getOperator(),operators.get(j).numofUsers+1,array.get(i).getMcc(),array.get(i).getMnc(),array.get(i).getUser());
								operators.get(j).numofUsers++;
								operators.get(j).users.add(array.get(i)
										.getUser());
								found = 1;
							}
							break;
						}
					}
					if (found == 0 && j == operators.size()) {
						Operator temp = new Operator();
						temp.setAll(array.get(i).getOperator(),
								temp.numofUsers + 1, array.get(i).getMcc(),
								array.get(i).getMnc(), array.get(i).getUser());
						operators.add(temp);
					}
				}
			}
		}
		ArrayList<String> finaldata = new ArrayList<String>();
		for (int i = 0; i < operators.size(); i++) {
			finaldata.add(operators.get(i).Name + "#"
					+ operators.get(i).numofUsers);
		}
		return finaldata;
	}

}
