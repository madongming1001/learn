package com.mdm.headfirst.observer.weather;

import java.util.ArrayList;
import java.util.List;

public class WeatherData implements Subject {
	private List<com.mdm.headfirst.observer.weather.Observer> observers;
	private float temperature;
	private float humidity;
	private float pressure;
	
	public WeatherData() {

		observers = new ArrayList();
	}
	@Override
	public void registerObserver(com.mdm.headfirst.observer.weather.Observer o) {
		observers.add(o);
	}
	@Override
	public void removeObserver(com.mdm.headfirst.observer.weather.Observer o) {
		observers.remove(o);
	}
	
	public void notifyObservers() {
		for (Observer observer : observers) {
			observer.update(temperature, humidity, pressure);
		}
	}
	
	public void measurementsChanged() {
		notifyObservers();
	}
	
	public void setMeasurements(float temperature, float humidity, float pressure) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		measurementsChanged();
	}

	public float getTemperature() {
		return temperature;
	}
	
	public float getHumidity() {
		return humidity;
	}
	
	public float getPressure() {
		return pressure;
	}

}
