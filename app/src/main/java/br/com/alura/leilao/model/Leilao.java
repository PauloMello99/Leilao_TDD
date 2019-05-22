package br.com.alura.leilao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.alura.leilao.exception.LanceMenorQueUltimoException;
import br.com.alura.leilao.exception.LanceSeguidodoMesmoUserException;
import br.com.alura.leilao.exception.UsuarioComMaisDeCincoLancesException;

public class Leilao implements Serializable {

    private final String descricao;
    private final List<Lance> lances;
    private double maiorLance = 0.0;
    private double menorLance = 0.0;

    public Leilao(String descricao) {
        this.descricao = descricao;
        this.lances = new ArrayList<>();
    }

    public void propoe(Lance lance){
        validaLance(lance);
        lances.add(lance);
        definePrimeiroLance(lance);
        Collections.sort(lances);
        calculaMaiorLance(lance);
    }

    private void definePrimeiroLance(Lance lance) {
        if(lances.size() == 1){
            maiorLance = lance.getValor();
            menorLance = lance.getValor();
        }
    }

    private void validaLance(Lance lance) {
        if(maiorLance > lance.getValor()) throw new LanceMenorQueUltimoException();
        if(!lances.isEmpty()){
            Usuario usuarioNovo = lance.getUsuario();
            Usuario ultimoUsuario = lances.get(0).getUsuario();
            if(usuarioNovo.equals(ultimoUsuario)) throw new LanceSeguidodoMesmoUserException();
            if (verificaQtdLances(usuarioNovo)) throw new UsuarioComMaisDeCincoLancesException();
        }
    }

    private boolean verificaQtdLances(Usuario usuarioNovo) {
        int lancesUsuario =0;
        for (Lance l : lances) {
            Usuario usuarioExistente = l.getUsuario();
            if(usuarioExistente.equals(usuarioNovo)){
                lancesUsuario++;
                if(lancesUsuario==5) return true;
            }
        }
        return false;
    }

    private void calculaMenorLance(Lance lance) {
        if(lance.getValor() < menorLance){
            menorLance = lance.getValor();
        }
    }

    private void calculaMaiorLance(Lance lance) {
        if(lance.getValor() > maiorLance){
            maiorLance = lance.getValor();
        }
    }

    public double getMenorLance() {
        return menorLance;
    }

    public double getMaiorLance() {
        return maiorLance;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<Lance> getTresMaioresLances() {
        if(lances.size()<=3) return lances.subList(0,lances.size());
        else return lances.subList(0,3);
    }

    public int getQuantidadeLances() {
        return lances.size();
    }
}