package GestionUsuarios.GestionUsuarios.mapper;

// Corrected DTO imports
import GestionUsuarios.GestionUsuarios.DTO.TipoUsuarioRequestDTO;
import GestionUsuarios.GestionUsuarios.DTO.TipoUsuarioResponseDTO;
// Corrected model import (assuming model is in this package structure)
import GestionUsuarios.GestionUsuarios.model.TipoUsuario;

import org.springframework.stereotype.Component;

@Component
public class TipoUsuarioMapper {

    public TipoUsuarioResponseDTO toResponseDTO(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            return null;
        }
        return new TipoUsuarioResponseDTO(
                tipoUsuario.getId(),
                tipoUsuario.getNombre()
        );
    }

    public TipoUsuario toEntity(TipoUsuarioRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setNombre(requestDTO.getNombre());
        return tipoUsuario;
    }

    public void updateEntityFromDto(TipoUsuarioRequestDTO requestDTO, TipoUsuario tipoUsuario) {
        if (requestDTO == null || tipoUsuario == null) {
            return;
        }
        tipoUsuario.setNombre(requestDTO.getNombre());
    }
}