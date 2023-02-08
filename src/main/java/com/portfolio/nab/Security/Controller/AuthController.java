
package com.portfolio.nab.Security.Controller;

import com.portfolio.nab.Security.Dto.JwtDto;
import com.portfolio.nab.Security.Dto.LoginUsuario;
import com.portfolio.nab.Security.Dto.NuevoUsuario;
import com.portfolio.nab.Security.Entity.Rol;
import com.portfolio.nab.Security.Entity.Usuario;
import com.portfolio.nab.Security.Enums.RolNombre;
import com.portfolio.nab.Security.Jwt.JwtProvider;
import com.portfolio.nab.Security.Service.RolService;
import com.portfolio.nab.Security.Service.UsuarioService;
import java.util.HashSet;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@CrossOrigin(origins = {"https://nabfrontend.web.app","http://localhost:4200"})
public class AuthController {
   @Autowired 
   PasswordEncoder passwordEncoder;
   @Autowired 
   AuthenticationManager authenticationManager; 
   @Autowired 
   UsuarioService usuarioService;
   @Autowired 
   RolService rolService;
   @Autowired 
   JwtProvider jwtProvider;
             
   @PostMapping("/auth/nuevo")
   @SuppressWarnings("unchecked")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){
    if(bindingResult.hasErrors())
     return new ResponseEntity<>(new Mensaje("Campos mal puestos o email invalido"),HttpStatus.BAD_REQUEST); 
    
    if(usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity<>(new Mensaje("Ese nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);
    
    if(usuarioService.existsBYEmail(nuevoUsuario.getEmail()))
        return new ResponseEntity<>(new Mensaje("Ese email ya existe"),HttpStatus.BAD_REQUEST);
    
    Usuario usuario = new Usuario(nuevoUsuario.getNombre(), nuevoUsuario.getNombreUsuario(),
      nuevoUsuario.getEmail(), passwordEncoder.encode(nuevoUsuario.getPassword()));
      Set<Rol> roles = new HashSet<>();
    roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
    if(nuevoUsuario.getRoles().contains("admin"))
        roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
    usuario.setRoles(roles);
    usuarioService.save(usuario);
    
    return new ResponseEntity<>(new Mensaje("Usuario Guardado"),HttpStatus.CREATED);
    
    } 
    @PostMapping("/auth/login")
   @SuppressWarnings("unchecked")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
    if(bindingResult.hasErrors())
    return new ResponseEntity<JwtDto>((MultiValueMap<String, String>) new Mensaje("Campos mal puestos"),HttpStatus.BAD_REQUEST);
    
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
    loginUsuario.getNombreUsuario(),loginUsuario.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    
    String jwt = jwtProvider.generateToken(authentication);
       
       UserDetails userDetails = (UserDetails) authentication.getPrincipal();
       
       JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(),userDetails.getAuthorities());
       return new ResponseEntity<>(jwtDto,HttpStatus.OK);
    }
}