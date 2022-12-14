package ec.edu.ista.springgc1.controller;

import ec.edu.ista.springgc1.exception.AppException;
import ec.edu.ista.springgc1.model.entity.AreaEstudio;
import ec.edu.ista.springgc1.model.entity.Rol;
import ec.edu.ista.springgc1.service.impl.AreaEstudioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/AreasEstudio")
public class AreaEstudioController {

    @Autowired
    private AreaEstudioServiceImpl areaEstudioService;


    @GetMapping
    ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(areaEstudioService.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(areaEstudioService.findById(id));
    }

    @PostMapping
    ResponseEntity<?> create(@Valid @RequestBody AreaEstudio areaEstudio) {

        if (areaEstudioService.findByNombre(areaEstudio.getNombre()).isPresent()){
            throw new AppException(HttpStatus.BAD_REQUEST,"El dato ingresado ya se encuentra registrado");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(areaEstudioService.save(areaEstudio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody AreaEstudio areaEstudio) {
        AreaEstudio areaEstudioFromDb = areaEstudioService.findById(id);

        if (!areaEstudio.getNombre().equalsIgnoreCase(areaEstudioFromDb.getNombre())&& areaEstudioService.findByNombre(areaEstudio.getNombre()).isPresent()){
            throw new AppException(HttpStatus.BAD_REQUEST,"El dato ingresado ya se encuentra registrado");
        }
        areaEstudioFromDb.setNombre(areaEstudio.getNombre());

        return ResponseEntity.status(HttpStatus.CREATED).body(areaEstudioService.save(areaEstudioFromDb));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        AreaEstudio areaEstudioFromDb = areaEstudioService.findById(id);
        areaEstudioService.delete(areaEstudioFromDb.getId());
        return ResponseEntity.noContent().build();
    }



}
