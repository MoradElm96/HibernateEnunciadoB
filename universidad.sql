drop database universidad; 
create database Universidad;
use Universidad; 

create table Especialidad(
   Cod int primary key,
   Nombre varchar(30)
);

create table Profesor (
   Id integer primary key,
   Nombre varchar(20),
   Apellidos varchar(30),
   cod int,
   foreign key(cod) references Especialidad(Cod)
   on delete cascade on update cascade 
);

 insert into Especialidad values(1,'Informatica');
 insert into Especialidad values(2,'FOL');
 insert into Especialidad values(3,'Ingles');
 insert into Especialidad values(4,'Administrativo');
 insert into Especialidad values(5,'Comercio');
 
 
 insert into Profesor values(1,'Juan','Garcia Lopez',1);
 insert into Profesor values(2,'Ramon','Real Gomez',2);
 insert into Profesor values(3,'Maria','Rodriguez Casado',3);
 insert into Profesor values(4,'Pepe','Moreno Garcia',1);
 insert into Profesor values(5,'Maria Jose','Sanz Diaz',4);
 insert into Profesor values(6,'Isidoro','Jimenez Mesa',4);
 insert into Profesor values(7,'Carmen','Rodriguez Conde',1);
 insert into Profesor values(8,'Enrique','Donaire Fraile',5);
 insert into Profesor values(9,'Irene','Molina Castro',3);
 insert into Profesor values(10,'Luis','Gago Silo',4);
 insert into Profesor values(11,'Mirian','Martin Yang',1);
 insert into Profesor values(12,'Santiago','Gil Hernandez',2);
    