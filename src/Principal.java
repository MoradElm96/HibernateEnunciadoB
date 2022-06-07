
import POJOS.Especialidad;
import POJOS.Profesor;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Ejecutar el fichero universidad.sql, que creara la base de datos Universidad,
 * compuesta de las tablas Especialidad y Profesor. Usando HIBERNATE hacer un
 * programa que actualice las tablas mediante el fichero binario
 * “datosprofesor.bin”. El fichero tiene la siguiente estructura:
 *
 * Id(int), nombre(cadena), apellidos(cadena), especialidad(cadena) y
 * operacion(int) El campo operación tendrá un 1 si es un alta, un 2 si es una
 * baja y 3 si es una modificación. Se deben tener en cuenta las siguientes
 * premisas: - Si es un alta y ya existe el profesor se informará del error. -
 * Si es un alta y la especialidad no existe, se debe preguntar si se desea dar
 * de alta y en caso afirmativo se dará de alta. El nombre será el que viene en
 * el fichero y el código será el ultimo de la tabla +10. - Si es una baja o una
 * modificación y el profesor no existe se informará del error. - Si es una
 * modificación y la especialidad no existe se debe proceder como en el caso 2.
 * Finalmente, mostrar en pantalla un informe que muestre el nombre de la
 * especialidad y a continuación los nombres de los profesores de dicha
 * especialidad.
 *
 * @author alumno
 */
public class Principal {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        SessionFactory sf = HibernateUtil.sessionFactory();
        Session s = sf.openSession();
        Transaction t = s.beginTransaction();

        //hacemos las consultas a las tablas y las mostramos por pantalla
        Query selectEspecialidad = s.createQuery("From Especialidad");
        Query selectProfesor = s.createQuery("From Profesor");
        List<Especialidad> listaEspecialidades = selectEspecialidad.list();
        List<Profesor> listaProfesores = selectProfesor.list();

        Profesor prof;
        Especialidad esp;

        System.out.println("--------------------Tabla profesor----------------------------");
        for (int i = 0; i < listaProfesores.size(); i++) {
            System.out.println(listaProfesores.get(i).Mostrar());

        }

        System.out.println("--------------------Tabla especialidades----------------------");
        for (int i = 0; i < listaEspecialidades.size(); i++) {
            System.out.println(listaEspecialidades.get(i).Mostrar());

            //preguntar iterador
            Iterator it = listaEspecialidades.get(i).getProfesors().iterator();
            while (it.hasNext()) {
                System.out.println(((Profesor) it.next()).Mostrar());//casteamos a clase profesor

            }

        }

        File f = new File("datosprofesor.bin");
        try {

            FileInputStream fis = new FileInputStream(f);
            DataInputStream dis = new DataInputStream(fis);

            //esto contiene el archivo
            int id;
            String nombre, apellidos, especialidad, preguntaDarAlta;
            int operacion;
            
            int codEspecialidad=0;
            int nuevoCodEspecialidad=0;

            try {
                while (true) {

                    id = dis.readInt();
                    nombre = dis.readUTF();
                    apellidos = dis.readUTF();
                    especialidad = dis.readUTF();
                    operacion = dis.readInt();

                    for (int i = 0; i < listaEspecialidades.size(); i++) { //obtenemos las especialidades que contengan el archivo
                        if (listaEspecialidades.get(i).getNombre() == especialidad) {
                            codEspecialidad = listaEspecialidades.get(i).getCod();

                        }
                    }
                    
                   System.out.println("ID = "+id+" | NOMBRE = "+nombre+" | APELLIDOS = "+apellidos
                            +" | EPECIALIDAD = "+especialidad+" | CodESPECIALIDAD = " + codEspecialidad+" | OPERACION = "+operacion);
                    if (operacion == 1) {
                        prof = (Profesor) s.get(Profesor.class, id);
                        if (prof != null){
                            System.out.println("El profesor ya está en la base de datos, no se puede insertar de nuevo.");
                            
                        } else {
                            esp = (Especialidad) s.get(Especialidad.class, codEspecialidad);
                            if (esp == null) {
                                System.out.println("¿Desea dar de alta al profesor.. "+nombre+" "+apellidos+"? ->Pulsa S [Si]  N [No]");
                                Scanner sc = new Scanner(System.in);
                                preguntaDarAlta = sc.nextLine();
                                if (preguntaDarAlta.equalsIgnoreCase("s")){
                                    nuevoCodEspecialidad = listaEspecialidades.get(listaEspecialidades.size()-1).getCod()+10;
                                    esp = new Especialidad(nuevoCodEspecialidad, especialidad);
                                    s.save(esp);
                                    prof = new Profesor(id, esp, nombre, apellidos);
                                    s.save(prof);
                                } else {
                                    System.out.println("No se dará de alta al profesor "+nombre+" "+apellidos);
                                }
                            } else{
                                prof = new Profesor(id, esp, nombre, apellidos);
                                s.save(prof);
                            }
                        }
                    } else if(operacion == 2){
                        prof = (Profesor) s.get(Profesor.class, id);
                        if (prof == null){
                            System.out.println("El profesor "+nombre+" "+apellidos+" no existe, no se puede dar de baja.");
                        } else {
                            System.out.println("Se va a borrar al profesor "+nombre+" "+apellidos);
                            s.delete(prof);
                        }
                    } else if(operacion == 3){
                        prof = (Profesor) s.get(Profesor.class, id);
                        if (prof == null) {
                            System.out.println("El profesor "+nombre+" "+apellidos+" no existe, no se puede modificar.");
                        } else {
                            esp = (Especialidad) s.get(Especialidad.class, codEspecialidad);
                            if(esp == null){
                                System.out.println("¿Desea dar de alta al profesor.. "+nombre+" "+apellidos+"? ->Pulsa S [Si]  N [No]");
                                Scanner sc = new Scanner(System.in);
                                preguntaDarAlta = sc.nextLine();
                                if (preguntaDarAlta.equalsIgnoreCase("s")){
                                    nuevoCodEspecialidad = listaEspecialidades.get(listaEspecialidades.size()-1).getCod()+10;
                                    esp = new Especialidad(nuevoCodEspecialidad, especialidad);
                                    s.save(esp);
                                    prof.setEspecialidad(esp);
                                    s.save(prof);
                                }
                            }
                        }
                    }
                    listaEspecialidades = selectEspecialidad.list();
                    listaProfesores = selectProfesor.list();
                    
                }
            }catch (EOFException e){
                System.out.println("Fin del fichero.");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("\nTABLA PROFESORES\n");
        
        for(int i=0;i<listaEspecialidades.size();i++){
            System.out.println(listaEspecialidades.get(i).Mostrar()); 
            Iterator it=listaEspecialidades.get(i).getProfesors().iterator();
            while(it.hasNext())
              System.out.println(((Profesor) it.next()).Mostrar());
        }
        
        t.commit();
        s.close();
        System.exit(0);

    }
}
