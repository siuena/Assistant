package com.coofee.assistant.util.bus;

import com.coofee.assistant.bus.BusLineResult;
import com.coofee.assistant.bus.BusStationResult;
import com.coofee.assistant.bus.BusTransferResult;

public interface BusListener {
	public void onTransfer(BusTransferResult transferResult);
	public void onLine(BusLineResult lineResult);
	public void onStation(BusStationResult stationResult);
	public void onError(String errorMessage);
}
