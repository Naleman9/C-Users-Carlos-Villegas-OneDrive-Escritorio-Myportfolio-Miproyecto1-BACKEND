
package com.portfolio.nab.Security.Controller;


public class Mensaje {
    private String mensaje;
    
//contructor
    public Mensaje(){
}

    public Mensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
