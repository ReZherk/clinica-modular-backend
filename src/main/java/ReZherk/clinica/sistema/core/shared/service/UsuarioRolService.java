package ReZherk.clinica.sistema.core.shared.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.RolPerfilRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioRolService {

 private final RolPerfilRepository rolPerfilRepository;

 public void assignRoleToUser(Usuario usuario, String roleName) {
  RolPerfil rol = findRolByName(roleName);

  Set<RolPerfil> roles = new HashSet<>();
  roles.add(rol);
  usuario.setPerfiles(roles);
 }

 public void assignRolesToUser(Usuario usuario, Set<String> roleNames) {
  Set<RolPerfil> roles = new HashSet<>();

  for (String roleName : roleNames) {
   RolPerfil rol = findRolByName(roleName);
   roles.add(rol);
  }

  usuario.setPerfiles(roles);
 }

 public void addRoleToUser(Usuario usuario, String roleName) {
  RolPerfil rol = findRolByName(roleName);
  usuario.getPerfiles().add(rol);
 }

 public void removeRoleFromUser(Usuario usuario, String roleName) {
  usuario.getPerfiles().removeIf(rol -> rol.getNombre().equals(roleName));
 }

 private RolPerfil findRolByName(String roleName) {
  return rolPerfilRepository.findByNombre(roleName)
    .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + roleName));
 }

}
