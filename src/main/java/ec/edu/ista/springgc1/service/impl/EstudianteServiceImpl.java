package ec.edu.ista.springgc1.service.impl;

import ec.edu.ista.springgc1.exception.ResourceNotFoundException;
import ec.edu.ista.springgc1.model.dto.EstudianteDTO;
import ec.edu.ista.springgc1.model.entity.Ciudad;
import ec.edu.ista.springgc1.model.entity.Estudiante;
import ec.edu.ista.springgc1.model.entity.Usuario;
import ec.edu.ista.springgc1.repository.CiudadRepository;
import ec.edu.ista.springgc1.repository.EstudianteRepository;
import ec.edu.ista.springgc1.repository.UsuarioRepository;
import ec.edu.ista.springgc1.service.bucket.S3Service;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstudianteServiceImpl extends GenericServiceImpl<Estudiante> implements Mapper<Estudiante, EstudianteDTO> {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private S3Service s3Service;

    @Override
    public Estudiante mapToEntity(EstudianteDTO estudianteDTO) {
        Estudiante estudiante = new Estudiante();

        Usuario usuario = usuarioRepository.findByUsername(estudianteDTO.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("username", estudianteDTO.getUsername()));

        Ciudad ciudad = ciudadRepository.findByNombre(estudianteDTO.getCiudad())
                .orElseThrow(() -> new ResourceNotFoundException("ciudad", estudianteDTO.getCiudad()));

        estudiante.setId(estudianteDTO.getId());
        estudiante.setUsuario(usuario);
        estudiante.setCedula(estudianteDTO.getCedula());
        estudiante.setNombres(estudianteDTO.getNombres());
        estudiante.setApellidos(estudianteDTO.getApellidos());
        estudiante.setGenero(estudianteDTO.getGenero());
        estudiante.setFechaNacimiento(estudianteDTO.getFechaNacimiento());
        estudiante.setCiudad(ciudad);
        estudiante.setDireccion(estudianteDTO.getDireccion());
        estudiante.setEstadoCivil(estudianteDTO.getEstadoCivil());
        estudiante.setRutaImagen(estudianteDTO.getRutaImagen());
        estudiante.setUrlImagen(estudianteDTO.getRutaImagen() == null ? null : s3Service.getObjectUrl(estudianteDTO.getRutaImagen()));

        return estudiante;
    }

    @Override
    public EstudianteDTO mapToDTO(Estudiante estudiante) {
        EstudianteDTO estudianteDTO = new EstudianteDTO();

        estudianteDTO.setId(estudiante.getId());
        estudianteDTO.setUsername(estudiante.getUsuario().getUsername());
        estudianteDTO.setCedula(estudiante.getCedula());
        estudianteDTO.setNombres(estudiante.getNombres());
        estudianteDTO.setApellidos(estudiante.getApellidos());
        estudianteDTO.setGenero(estudiante.getGenero());
        estudianteDTO.setFechaNacimiento(estudiante.getFechaNacimiento());
        estudianteDTO.setCiudad(estudiante.getCiudad().getNombre());
        estudianteDTO.setDireccion(estudiante.getDireccion());
        estudianteDTO.setEstadoCivil(estudiante.getEstadoCivil());
        estudianteDTO.setRutaImagen(estudiante.getRutaImagen());
        estudianteDTO.setUrlImagen(estudiante.getUrlImagen());

        return estudianteDTO;
    }

    @Override
    public List findAll() {
        return estudianteRepository.findAll()
                .stream()
                .peek(e -> e.setUrlImagen(e.getRutaImagen() == null ? null : s3Service.getObjectUrl(e.getRutaImagen())))
                .map(e -> mapToDTO(e))
                .collect(Collectors.toList());
    }

    public EstudianteDTO findByIdToDTO(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id", id));

        return mapToDTO(estudiante);
    }

    public EstudianteDTO findByUsuario(long usuario_id) {

        Estudiante estudiante = estudianteRepository.findByUsuario(usuario_id)
                .orElseThrow(() -> new ResourceNotFoundException("usuario_id", usuario_id));
        estudiante.setUrlImagen(estudiante.getRutaImagen() == null ? null : s3Service.getObjectUrl(estudiante.getRutaImagen()));
        return mapToDTO(estudiante);
    }

    public Optional<Estudiante> findByCedula(String cedula) {

        return estudianteRepository.findByCedula(cedula);
    }

    public Boolean existsByCedula(String cedula) {
        return estudianteRepository.existsByCedula(cedula);
    }

    public EstudianteDTO findByCedulaToDTO(String cedula) {
        Estudiante estudiante = estudianteRepository.findByCedula(cedula).orElseThrow(() -> new ResourceNotFoundException("cedula", cedula));
        return mapToDTO(estudiante);
    }

    @Override
    public Estudiante save(Object entity) {

        return estudianteRepository.save(mapToEntity((EstudianteDTO) entity));
    }

    public Long countEstudiantes(){
        return estudianteRepository.count();
    }
}
