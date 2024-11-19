public class ArbolPrueba {
    NodoArbUsuarios raizUsuarios;
    NodoArbCanciones raizCanciones;
    NodoListaAutores listaAutores;
    NodoListaPropias listaPropia;
    
    public ArbolPrueba (){
        raizUsuarios = null;
        raizCanciones = null;
        listaAutores = null;
        listaPropia = null;
    }
    
    //menu 1
    
    public void crearUsuario (String nombreUsuario, String password){
        raizUsuarios = crearUsuarioRec(raizUsuarios,nombreUsuario,password);
    }
    
    private NodoArbUsuarios crearUsuarioRec(NodoArbUsuarios actual, String nombreUsuario, String password){
        
        if(verificarSiexisteUsuario(actual,nombreUsuario,password)){
            System.out.println("el usuario ingresado ya existe ");
        }else if (password.length() >= 6 && password.length() <= 8){
            if (actual == null){
                actual = new NodoArbUsuarios (nombreUsuario, password);
            }else if (actual.getNombreUsuario().compareTo(nombreUsuario) <0 ){
                actual.setMenores(crearUsuarioRec(actual.getMenores(), nombreUsuario, password)); 
            }else if (actual.getNombreUsuario().compareTo(nombreUsuario) >= 0){
                actual.setMayores(crearUsuarioRec(actual.getMayores(), nombreUsuario, password));
            }
        }else {
            System.out.println("la contrasenia ingresada debe tener mas de 6 caracteres y menos de 8 ");
        }  
        return actual;
    }
    
    public boolean verificarSiexisteUsuario(NodoArbUsuarios actual, String nombreUsuario, String password){
            if (actual == null){
                return false;
            } else if (nombreUsuario.compareTo(actual.getNombreUsuario()) > 0){
                return verificarSiexisteUsuario(actual.getMenores(), nombreUsuario, password);
            }else if (nombreUsuario.compareTo(actual.getNombreUsuario()) < 0){
                return verificarSiexisteUsuario(actual.getMayores(), nombreUsuario, password);
            }else
                return true;
    }
        
    public void mostrarArbolUsuarios(){
        mostrarRecArbolUsuarios(raizUsuarios);
    }
        
    private void mostrarRecArbolUsuarios(NodoArbUsuarios actual){
        if (actual != null){
            mostrarRecArbolUsuarios(actual.getMenores());
            System.out.println("el usuario es: " +actual.getNombreUsuario());
            mostrarRecArbolUsuarios(actual.getMayores());
        }
    }
    

    public boolean login(String nombreUsuario, String password){
        return loginRec(raizUsuarios, nombreUsuario, password);
    }
    
    private boolean loginRec(NodoArbUsuarios actual, String nombreUsuario, String password){
        if (verificarSiexisteUsuario(actual, nombreUsuario, password )){
            if (actual.getPassword().equals(password)){
                System.out.println("Usuario ingresado correctamente");
                return true;
            } else
                System.out.println("contrasenia invalida. ");
        } else{
            System.out.println("El usuario ingresado no existe");
        }
        return false;
    }
 ////////////////1    
    
    
    public void agregarCancion(String titulo, String autor){
        listaAutores = VincularAutores(listaAutores, titulo, autor);
        raizCanciones = agregarCancionRec(raizCanciones, titulo, autor);
    } 
    private NodoArbCanciones agregarCancionRec(NodoArbCanciones actual, String titulo,String autor){
        NodoListaAutores nodoAutor = new NodoListaAutores(autor,actual);
        if (actual == null){
            actual =new NodoArbCanciones(titulo);
        }else if (verificarNombreCancion (actual,titulo) && verificarAutor(nodoAutor, autor)){
            System.out.println("no te agrego un carajo");
        }else if (actual.getTitulo().compareTo(titulo)<0){
            actual.setMenores(agregarCancionRec(actual.getMenores(),titulo, autor));
        }else{ 
            actual.setMayores(agregarCancionRec(actual.getMayores(),titulo, autor));
        } 
        return actual;
    }//actual.setArbolCanciones(nuevaCancion);
    //if(verificarAutor(actual,  autor) && !verificarNombreCancion(nuevaCancion, titulo)){
      //      //agrega la cancion pero no el autor
        //}
    private NodoListaAutores VincularAutores(NodoListaAutores actual, String titulo, String autor){
        NodoArbCanciones nuevaCancion = new NodoArbCanciones(titulo);
        NodoListaAutores nodoAutor = new NodoListaAutores(autor,nuevaCancion);
        if (actual == null){
            actual = nodoAutor;
        }else if(actual.getAutor().compareTo(nodoAutor.getAutor()) > 0){
            nodoAutor.setSiguiente(actual);
            actual = nodoAutor;
        } else {
            actual.setSiguiente(VincularAutores(actual.getSiguiente(),titulo,autor));
        }
        return actual;
    }
    
    private boolean verificarAutor(NodoListaAutores actual, String autor){
        if (actual  == null){
            return false;
        } else if (actual.getAutor().equals(autor)){
            return true;
        } else 
             verificarAutor(actual.getSiguiente(),autor);
        return false;
    }
    
    private boolean verificarNombreCancion (NodoArbCanciones actual, String titulo){
        if(actual == null){
            return false;
        }else if(actual.getTitulo().equals(titulo)){
            return true;
        }else if (actual.getTitulo().compareTo(titulo) <0){
            return verificarNombreCancion(actual.getMenores(),titulo);
        }else
            return verificarNombreCancion(actual.getMayores(),titulo);
    }
        
    
    public void mostrarArbolCanciones() {
        System.out.println("Las canciones son: ");
        mostrarArbolCancionesRec(raizCanciones);
    }

    private void mostrarArbolCancionesRec(NodoArbCanciones actual) {
        if (actual != null) {
            mostrarArbolCancionesRec(actual.getMenores());
            System.out.println("Titulo: " + actual.getTitulo());
            mostrarArbolCancionesRec(actual.getMayores());
        }
    }

    public void mostrarListaAutores(){
        System.out.println("Los autores son: ");
        NodoListaAutores actual = listaAutores; 
        while (actual !=null){
            System.out.println("Autor: " + actual.getAutor());
            actual= actual.getSiguiente();
        }
    }



////////////////2  
    
    
    public void crearListaPropia(String nombrePlaylist) {
        raizUsuarios = crearListaPropiaRec(raizUsuarios, nombrePlaylist);
    }

    private NodoArbUsuarios crearListaPropiaRec(NodoArbUsuarios actual, String nombrePlaylist) {
        NodoArbUsuarios usuario = buscaUsuario (actual , nombrePlaylist);
        if (usuario != null) {
                if (verificarPlaylist(actual.getListaPropias(), nombrePlaylist)) {
                    System.out.println("Este nombre ya existe para este usuario");
                } else {
                    actual.setListaPropias(agregarListaPropia(actual.getListaPropias(),nombrePlaylist));
                }
            } else if (usuario.getNombreUsuario().compareTo(actual.getNombreUsuario()) < 0) {
                actual.setMenores(crearListaPropiaRec(actual.getMenores(), nombrePlaylist));
            } else {
                actual.setMayores(crearListaPropiaRec(actual.getMayores(), nombrePlaylist));
            }
            return actual;
    }
    
    public NodoArbUsuarios buscaUsuario(NodoArbUsuarios actual, String nombreUsuario){
            if (actual == null){
                return actual;
            } else if (nombreUsuario.compareTo(actual.getNombreUsuario()) > 0){
                return buscaUsuario(actual.getMenores(), nombreUsuario);
            }else if (nombreUsuario.compareTo(actual.getNombreUsuario()) < 0){
                return buscaUsuario(actual.getMayores(), nombreUsuario);
            }else
                return actual;
    }

    private NodoListaPropias agregarListaPropia(NodoListaPropias actual, String nombrePlaylist) {
        NodoListaPropias nuevaPlaylist = new NodoListaPropias(nombrePlaylist);
        if (actual == null) {
            return nuevaPlaylist;
        } else if (actual.getNombrePlaylist().compareTo(nombrePlaylist) > 0) {
            nuevaPlaylist.setSigPlaylist(actual);
            return nuevaPlaylist;
        } else {
            actual.setSigPlaylist(agregarListaPropia(actual.getSigPlaylist(), nombrePlaylist));
            return actual;
        }
    }

    private boolean verificarPlaylist(NodoListaPropias actual, String nombrePlaylist) {
        if (actual == null) {
            return false;
        } else if (actual.getNombrePlaylist().equals(nombrePlaylist)) {
            return true;
        } else {
            return verificarPlaylist(actual.getSigPlaylist(), nombrePlaylist);
        }
    }

    
    
    /*public void crearListaPropia(String nombrePlaylist){
       // crearListaPropia(raizUsuarios,nombrePlaylist);
    }
    public void crearListaPropia(String nombreUsuario, String nombrePlaylist) {
    NodoArbUsuarios usuario = buscaUsuario(raizUsuarios, nombreUsuario);
    if (usuario != null) {
        if (verificarPlaylistUsuario(nombreUsuario, nombrePlaylist)) {
            System.out.println("Este nombre ya existe para este usuario.");
        } else {
            usuario.setListaPropias(agregarListaPropia(usuario.getListaPropias(), nombrePlaylist));
        }
    } else {
        System.out.println("El usuario no existe.");
    }
    }
     
    
    private NodoListaPropias agregarListaPropia(NodoListaPropias actual , String nombrePlaylist){
        NodoListaPropias nuevaPlaylist = new NodoListaPropias(nombrePlaylist);
        if (actual  == null){
            actual = nuevaPlaylist;
        }else if (actual.getNombrePlaylist().compareTo(nombrePlaylist) > 0){
                nuevaPlaylist.setSigPlaylist(actual);
                actual = nuevaPlaylist;
            }else { // (actual.getNombrePlaylist().compareTo(nombrePlaylist) < 0)
                actual.setSigPlaylist(agregarListaPropia(actual.getSigPlaylist(),nombrePlaylist));
            }
        return actual;
    }
    private boolean verificarPlaylistUsuario (String nombreUsuario, String nombrePlaylist ){
        NodoArbUsuarios usuario = buscaUsuario(raizUsuarios, nombreUsuario);
        if (usuario != null ){
            return verificarPlaylist (usuario.getListaPropias(),nombrePlaylist);
        }else {
            return false;
        }
    }
  
    public NodoArbUsuarios buscaUsuario(NodoArbUsuarios actual, String nombreUsuario){
            if (actual == null){
                return actual;
            } else if (nombreUsuario.compareTo(actual.getNombreUsuario()) > 0){
                return buscaUsuario(actual.getMenores(), nombreUsuario);
            }else if (nombreUsuario.compareTo(actual.getNombreUsuario()) < 0){
                return buscaUsuario(actual.getMayores(), nombreUsuario);
            }else
                return actual;
    }
    
    private boolean verificarPlaylist (NodoListaPropias actual, String nombrePlaylist){
        if (actual == null){
           return false ;
       }else if(actual.getNombrePlaylist().equals(nombrePlaylist)){
           return true;
       }else {
            return verificarPlaylist(actual.getSigPlaylist(), nombrePlaylist);
        }
    }
    
    */
    

    
    
}   
    
    //listaPropia = crearListaPropia(listaPropia, nombrePlaylist)
    /*public void crearListaPropia (String nombrePlaylist){
        listaPropia = crearListaPropiaRec(listaPropia, nombrePlaylist);
    }
    private NodoListaPropias crearListaPropiaRec(NodoListaPropias actual, String nombrePlaylist){
        if (actual  == null){
            actual = new NodoListaPropias (nombrePlaylist);
        }else if (!verificarPlaylist(actual,nombrePlaylist)){    
            if (actual.getNombrePlaylist().compareTo(nombrePlaylist) < 0){
                actual.setSigPlaylist(crearListaPropiaRec(actual.getSigPlaylist(),nombrePlaylist));
            }else if (actual.getNombrePlaylist().compareTo(nombrePlaylist) > 0){
                System.out.println("jiji");
            }else 
            {
                System.out.println("Hay Cositas");
            }
            return actual;
        }
        return actual;
    }
    
    private boolean verificarPlaylist (NodoListaPropias nodo ,String nombrePlaylist){
        return verificarPlaylistRec(raizUsuarios, nodo , nombrePlaylist);
    }
    
    private boolean verificarPlaylistRec(NodoArbUsuarios actual ,NodoListaPropias nodo, String nombrePlaylist){
        if (actual == null){
           return false ;
       }else if(actual.getListaPropias().equals(nodo.getNombrePlaylist())){
           return true;
       }else 
       return false;
    }
    
    //exitenombreenListaPLaylist
    
    
    */
