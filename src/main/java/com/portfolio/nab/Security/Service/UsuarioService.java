
package com.portfolio.nab.Security.Service;

import com.portfolio.nab.Security.Entity.Usuario;
import com.portfolio.nab.Security.Repository.iUsuarioRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class UsuarioService {
    @Autowired
    iUsuarioRepository iusuarioRepository;
    
    public Optional<Usuario> getByNombreUsuario(String nombreUsuario){
    return iusuarioRepository.findByNombreUsuario(nombreUsuario);
    }
    
    public boolean existsByNombreUsuario(String nombreUsuario){
     return iusuarioRepository.existsByNombreUsuario(nombreUsuario);
    }
    public boolean existsBYEmail(String email){
     return iusuarioRepository.existsByNombreUsuario(email);
    }
    public void save(Usuario usuario){
        iusuarioRepository.save(usuario);
    }
}
