package com.researchmobile.todoterreno.pedidos.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Fecha {
	private Calendar calendar = Calendar.getInstance();
	
	public int semanaMes (){
		int semana = getCalendar().get(Calendar.WEEK_OF_MONTH);
		
		return semana;
	}
	
	public int semanaAnio() {
		int semana = getCalendar().get(Calendar.WEEK_OF_YEAR);
		
		return semana;
	}
	
	public int diaAnio(){
		int dia = Calendar.DAY_OF_YEAR;
		return dia;
	}
	
	public String diaLetra() {
		String dia = null;
		if (getCalendar().get(Calendar.DAY_OF_WEEK) == 1){
        	dia = "D";
        }else if (getCalendar().get(Calendar.DAY_OF_WEEK) == 2){
        	dia = "L";
        }else if (getCalendar().get(Calendar.DAY_OF_WEEK) == 3){
        	dia = "M";
        } else if (getCalendar().get(Calendar.DAY_OF_WEEK) == 4){
        	dia = "K";
        }else if (getCalendar().get(Calendar.DAY_OF_WEEK) == 5){
        	dia = "J";
        }else if (getCalendar().get(Calendar.DAY_OF_WEEK) == 6){
        	dia = "V";
        }else if (getCalendar().get(Calendar.DAY_OF_WEEK) == 7){
        	dia = "S";
        }
		
		return dia;
	}
	
	public String Dia (){
		String dia = null;
		if (getCalendar().get(Calendar.DAY_OF_WEEK) == 1){
        	dia = "Domingo";
        }else if (getCalendar().get(Calendar.DAY_OF_WEEK) == 2){
        	dia = "Lunes";
        }else if (getCalendar().get(Calendar.DAY_OF_WEEK) == 3){
        	dia = "Martes";
        } else if (getCalendar().get(Calendar.DAY_OF_WEEK) == 4){
        	dia = "Miercoles";
        }else if (getCalendar().get(Calendar.DAY_OF_WEEK) == 5){
        	dia = "Jueves";
        }else if (getCalendar().get(Calendar.DAY_OF_WEEK) == 6){
        	dia = "Viernes";
        }else if (getCalendar().get(Calendar.DAY_OF_WEEK) == 7){
        	dia = "Sabado";
        }
		
		return dia;
	}
	
	public String FechaHoy () {
		int dia = 0;
		int mes = 0;
		int anio = 0;
		String fecha = null;
		
		dia = getCalendar().get(Calendar.DAY_OF_MONTH);
		mes = getCalendar().get(Calendar.MONTH ) + 1;
		anio = getCalendar().get(Calendar.YEAR);
		fecha = ""+dia+"/"+mes+"/"+anio;
		
		return fecha;
	}
	
	public String fechaInversa(){
		int dia = 0;
		int mes = 0;
		int anio = 0;
		String fecha = null;
		
		dia = getCalendar().get(Calendar.DAY_OF_MONTH);
		mes = getCalendar().get(Calendar.MONTH ) + 1;
		anio = getCalendar().get(Calendar.YEAR);
		
		
		String a = String.valueOf(anio);
		String m = "";
		String d = "";
		
		if (mes < 10){
			m = "0" + mes;
		}else{
			m = String.valueOf(mes);
		}
		
		if (dia < 10){
			d = "0" + dia;
		}else{
			d = String.valueOf(dia);
		}
		
		fecha = a+"-"+m+"-"+d;
		
		return fecha;
	}
	public String fechaUnida(){
		int dia = 0;
		int mes = 0;
		int anio = 0;
		String fecha = null;
		
		dia = getCalendar().get(Calendar.DAY_OF_MONTH);
		mes = getCalendar().get(Calendar.MONTH ) + 1;
		anio = getCalendar().get(Calendar.YEAR);
		fecha = ""+dia+mes+anio;
		
		return fecha;
	}
	
	public String Hora(){
		String hora = null;
		String minuto = null;
		String segundo = null;
		
		String horaFinal = null;
		
		hora = "" + getCalendar().get(Calendar.HOUR);
		minuto = "" + getCalendar().get(Calendar.MINUTE);
		
		horaFinal = hora + ":" + minuto;
		
		return horaFinal;
	}
	
	public String semanaSiguiente(){
		int dia = 0;
		//int mes = 0;
		//int anio = 0;
		String fecha = null;
		dia = getCalendar().get(Calendar.DAY_OF_MONTH);
		//mes = getCalendar().get(Calendar.MONTH);
		//fecha = ""+anio+"/"+mes+"/"+(dia + 3);
		
		getCalendar().set(Calendar.DAY_OF_MONTH, dia + 4);
		Date f = getCalendar().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		fecha = sdf.format(f);
		return fecha;
	}
	
	public Calendar getCalendar() {
		return calendar;
	}
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
}
