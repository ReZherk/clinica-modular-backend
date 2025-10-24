-- Agrega la columna 'Horas_Semanales' para registrar el total de horas que trabajará el médico

ALTER TABLE Medico_Detalle
ADD COLUMN Horas_Semanales INT NOT NULL DEFAULT 0;