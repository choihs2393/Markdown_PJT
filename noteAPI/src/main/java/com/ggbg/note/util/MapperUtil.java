package com.ggbg.note.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {

private ModelMapper modelMapper;
    
    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper=modelMapper;
    }

	public <D, E> D convertToDTO(E from, Class<? extends D> to) {
        return modelMapper.map(from, to);
    }
}
