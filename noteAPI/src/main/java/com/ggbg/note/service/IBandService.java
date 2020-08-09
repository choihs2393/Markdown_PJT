package com.ggbg.note.service;

import com.ggbg.note.domain.dto.BandDTO;

public interface IBandService {
	
	public BandDTO addBand(String bandName, int accountNo);

	public int deleteBand(int bandNo, int accountNo);
	public int renameBand(String newBandName, int bandNo, int accountNo);
}
