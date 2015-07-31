package com.netmanagement.dataprocessing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.expression.spel.ast.OpAnd;

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
		String Name;
		int numofUsers=0;
		int mcc;
		int mnc;
		ArrayList<String> users = new ArrayList<String>();
		
		void setAll(String operator, int i, int mcc, int mnc, String user){
			System.out.println(Name+" | "+numofUsers+" | "+mcc+" | "+mnc+" | "+users);
			this.Name = operator;
			this.numofUsers = i;
			this.mcc = mcc;
			this.mnc = mnc;
			this.users.add(user);
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
						temp.setAll(array.get(i).getOperator(),temp.numofUsers+1,array.get(i).getMcc(),array.get(i).getMnc(),array.get(i).getUser());
						operators.add(temp);
						continue;
					}
					int found=0;
					int j=0;
					for (j=0;j<operators.size();j++){
						if ((operators.get(j).mcc == array.get(i).getMcc()) && (operators.get(j).mnc == array.get(i).getMnc())){
							if (!operators.get(j).users.contains(array.get(i).getUser())){
								//operators.get(j).setAll(array.get(i).getOperator(),operators.get(j).numofUsers+1,array.get(i).getMcc(),array.get(i).getMnc(),array.get(i).getUser());
								operators.get(j).numofUsers++;
								operators.get(j).users.add(array.get(i).getUser());
								found=1;
							}
							break;
						}
					}
					if (found==0 && j==operators.size()){
						Operator temp = new Operator();
						temp.setAll(array.get(i).getOperator(),temp.numofUsers+1,array.get(i).getMcc(),array.get(i).getMnc(),array.get(i).getUser());
						operators.add(temp);
					}
				}
			}
		}
		ArrayList<String> finaldata = new ArrayList<String>();
		for (int i=0;i<operators.size();i++){
			finaldata.add(operators.get(i).Name+"#"+operators.get(i).numofUsers);
		}
		return finaldata;
	}
	
}
