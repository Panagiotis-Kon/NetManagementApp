package com.netmanagement.dataprocessing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	
	private BaseStationsCalculations(){}
	
	public static BaseStationsCalculations getInstance(){
		if(BaseStationsCalculationsinstance == null){
			BaseStationsCalculationsinstance = new BaseStationsCalculations();
		}
		return BaseStationsCalculationsinstance;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<BaseStations> searchUser(String userID, String startDate, String endDate){
		
		System.out.println("userID: " + userID + " startDate: " + startDate + " endDate: " + endDate);
		HashMap<String, ArrayList<BaseStations>> hap = ParseBaseStations.getInstance().getHap();
		ArrayList<BaseStations> alist = new ArrayList<BaseStations>();
		if (!hap.isEmpty()){
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while(it.hasNext()){
				Map.Entry me = (Map.Entry)it.next();
				//System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
				ArrayList<BaseStations> array = (ArrayList<BaseStations>) me.getValue();
				for (int i=0;i<array.size();i++){
					BaseStations tempap = array.get(i);
					if (tempap.getUser().equals(userID)){
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Date date1 = sdf.parse(startDate);
							Date date2 = sdf.parse(endDate);
							Date dateu = sdf.parse(tempap.getTimestamp());
							//System.out.println(date1+" | "+date2+" | "+dateu);
							if (date1.equals(dateu) || date1.before(dateu)){
								if (date2.equals(dateu) || date2.after(dateu)){
									System.out.println(date1+" | "+date2+" | "+dateu);
									if (!tempap.getBSlatitude().equals("No Latitude") || !tempap.getBSlongtitude().equals("No longitude")){
										alist.add(tempap);
									}
								}
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			}
		}
		if(alist.isEmpty()){
			System.out.println("BaseStationsCalculations: alist is empty!!!");
		}
		return alist;
	}
	
	class Operator {
		String Operator;
		int numofUsers=0;
		ArrayList<String> users = new ArrayList<String>();
		
		void setAll(String operator, int i, String user){
			this.Operator = operator;
			this.numofUsers = i;
			this.users.add(user);
		}

		public String getOperator() {
			return Operator;
		}

		public void setOperator(String operator) {
			Operator = operator;
		}

		public int getNumofUsers() {
			return numofUsers;
		}

		public void setNumofUsers(int numofUsers) {
			this.numofUsers = numofUsers;
		}

		public ArrayList<String> getUsers() {
			return users;
		}

		public void setUsers(ArrayList<String> users) {
			this.users = users;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<String> Operators_numofUsers(){
		
		HashMap<String, ArrayList<BaseStations>> hap = ParseBaseStations.getInstance().getHap();
		ArrayList<Operator> operators = new ArrayList<Operator>();
		if (!hap.isEmpty()){
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while(it.hasNext()){
				Map.Entry me = (Map.Entry)it.next();
				//System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
				ArrayList<BaseStations> array = (ArrayList<BaseStations>) me.getValue();
				for (int i=0;i<array.size();i++){
					if (operators.size()==0){
						Operator temp = new Operator();
						temp.setAll(array.get(i).getOperator(),temp.getNumofUsers()+1,array.get(i).getUser());
						operators.add(temp);
					}
					else {
						if (operators.contains(array.get(i).getOperator())){
							if (!operators.get(operators.indexOf(array.get(i).getOperator())).users.contains(array.get(i).getUser())){
								operators.get(operators.indexOf(array.get(i).getOperator())).users.add(array.get(i).getUser());
								operators.get(operators.indexOf(array.get(i).getOperator())).numofUsers++;
							}
						}
						else {
							Operator temp = new Operator();
							temp.setAll(array.get(i).getOperator(),temp.getNumofUsers()+1,array.get(i).getUser());
							operators.add(temp);
						}
					}
				}
			}
		}
		ArrayList<String> finaldata = new ArrayList<String>();
		for (int i=0;i<operators.size();i++){
			finaldata.add(operators.get(i).Operator+"#"+operators.get(i).getNumofUsers());
		}
		return finaldata;
	}
	
}
