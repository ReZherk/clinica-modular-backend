-- Relación rol - opcionmenu (acciones)
CREATE TABLE IF NOT EXISTS perfil_opcionmenu (
    Id_RolOpcionMenu INT AUTO_INCREMENT PRIMARY KEY,
    Id_Perfil INT NOT NULL,
    Id_OpcionMenu INT NOT NULL,
    FOREIGN KEY (Id_Perfil) REFERENCES roles_perfil (Id_Perfil),
    FOREIGN KEY (Id_OpcionMenu) REFERENCES opcionmenu (Id_OpcionMenu)
);