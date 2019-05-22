package br.com.alura.leilao.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import br.com.alura.leilao.exception.LanceMenorQueUltimoException;
import br.com.alura.leilao.exception.LanceSeguidodoMesmoUserException;
import br.com.alura.leilao.exception.UsuarioComMaisDeCincoLancesException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class LeilaoTest {

    //Test Driven Development [TDD]

    private final Leilao LEILAO = new Leilao("Console");
    private final Usuario USUARIO = new Usuario("Paulo");

    @Rule
    public ExpectedException exception = ExpectedException.none();

    //folga que existe entre um double e outro que é tolerável
    private double DELTA = 0.0001;

    // Nomeação do método de teste para diferentes cenários
    // [nome do método] _ [estado de teste] _ [resultado esperado]
    // [deve] _ [resultado esperado] _ [estado de teste]

    @Test
    public void deve_DevolverDesc_QuandoRecebeDesc() {
        String returnDesc = LEILAO.getDescricao();
        assertThat(returnDesc,is("Console"));
    }

    @Test
    public void deve_DevolverMaiorLance_QuandoRecebeUmLance(){
        LEILAO.propoe(new Lance(USUARIO,200.0));
        assertEquals(200.0, LEILAO.getMaiorLance(), DELTA);
    }

    @Test
    public void deve_DevolverMaiorLance_QuandoRecebeLanceCrescente(){
        LEILAO.propoe(new Lance(USUARIO,100.0));
        LEILAO.propoe(new Lance(new Usuario("Alex"),200.0));
        assertEquals(200.0, LEILAO.getMaiorLance(), DELTA);
    }

    @Test(expected = LanceMenorQueUltimoException.class)
    public void deve_DevolverMaiorLance_QuandoRecebeLanceDecrescente(){
        LEILAO.propoe(new Lance(USUARIO,200.0));
        LEILAO.propoe(new Lance(USUARIO, 100.0));
    }

    @Test
    public void deve_DevolverMenorLance_QuandoRecebeUmLance(){
        LEILAO.propoe(new Lance(USUARIO,200.0));
        assertEquals(200.0, LEILAO.getMenorLance(), DELTA);
    }

    @Test
    public void deve_DevolverMenorLance_QuandoRecebeLanceCrescente(){
        LEILAO.propoe(new Lance(USUARIO,100.0));
        LEILAO.propoe(new Lance(new Usuario("FRAN"),200.0));
        assertEquals(100.0, LEILAO.getMenorLance(), DELTA);
    }

    @Test
    public void deve_Devolver_TresMaioresLances_QuandoNaoRecebeLances(){
        List<Lance> tresMaioresLances = LEILAO.getTresMaioresLances();
        assertEquals(0,tresMaioresLances.size());
    }

    @Test
    public void deve_Devolver_TresMaioresLances_QuandoRecebeUmLance(){
        LEILAO.propoe(new Lance(USUARIO,150.0));
        List<Lance> tresMaioresLances = LEILAO.getTresMaioresLances();

        assertEquals(1,tresMaioresLances.size());
        assertEquals(150.0,tresMaioresLances.get(0).getValor(),DELTA);
    }

    @Test
    public void deve_Devolver_TresMaioresLances_QuandoRecebeDoisLances(){
        LEILAO.propoe(new Lance(USUARIO,150.0));
        LEILAO.propoe(new Lance(new Usuario("Lucas"),200.0));
        List<Lance> tresMaioresLances = LEILAO.getTresMaioresLances();

        assertEquals(2,tresMaioresLances.size());
        assertEquals(200.0,tresMaioresLances.get(0).getValor(), DELTA);
        assertEquals(150.0,tresMaioresLances.get(1).getValor(), DELTA);
    }

    @Test
    public void deve_DevolverTresMaioresLances_QuandoRecebeTresLances(){
        LEILAO.propoe(new Lance(new Usuario("Fran"),100.0));
        LEILAO.propoe(new Lance(USUARIO,150.0));
        LEILAO.propoe(new Lance(new Usuario("Lucas"),200.0));
        List<Lance> tresMaioresLances = LEILAO.getTresMaioresLances();

        assertEquals(3,tresMaioresLances.size());
        assertEquals(200.0,tresMaioresLances.get(0).getValor(), DELTA);
        assertEquals(150.0,tresMaioresLances.get(1).getValor(), DELTA);
        assertEquals(100.0,tresMaioresLances.get(2).getValor(), DELTA);
    }

    @Test
    public void deve_DevolverTresMaioresLances_QuandRecebeMaisDeTresLances(){
        LEILAO.propoe(new Lance(new Usuario("Fran"),100.0));
        LEILAO.propoe(new Lance(USUARIO,150.0));
        LEILAO.propoe(new Lance(new Usuario("Lucas"),200.0));
        LEILAO.propoe(new Lance(new Usuario("Fran"),300.0));
        List<Lance> tresMaioresLances = LEILAO.getTresMaioresLances();

        assertEquals(3,tresMaioresLances.size());
        assertEquals(300.0,tresMaioresLances.get(0).getValor(), DELTA);
        assertEquals(200.0,tresMaioresLances.get(1).getValor(), DELTA);
        assertEquals(150.0,tresMaioresLances.get(2).getValor(), DELTA);

    }

    @Test
    public void deve_DevolverZeroMaiorLance_QuandoNaoTiverLance(){
        double maiorLance = LEILAO.getMaiorLance();
        assertEquals(0.0,maiorLance,DELTA);
    }

    @Test
    public void deve_DevolverZeroMenorLance_QuandoNaoTiverLance(){
        double menorLance = LEILAO.getMenorLance();
        assertEquals(0.0,menorLance,DELTA);
    }

    @Test(expected = LanceMenorQueUltimoException.class)
    public void naoDeve_AdicionarLance_QuandoForMenorQueMaiorLance(){

        LEILAO.propoe(new Lance(USUARIO,500));
        LEILAO.propoe(new Lance(new Usuario("FRAN"), 400));
    }

    @Test(expected = LanceSeguidodoMesmoUserException.class)
    public void naoDeve_AdicionarLance_QuandoUltimoLanceForDoMesmoUsuario(){

        LEILAO.propoe(new Lance(USUARIO,500));
        LEILAO.propoe(new Lance(USUARIO, 550));
    }

    @Test(expected = UsuarioComMaisDeCincoLancesException.class)
    public void naoDeve_AdicionarLance_QuandoUserDerCincoLances(){

        final Usuario FRAN = new Usuario("FRAN");
        LEILAO.propoe(new Lance(USUARIO,100));
        LEILAO.propoe(new Lance(FRAN,200));
        LEILAO.propoe(new Lance(USUARIO,300));
        LEILAO.propoe(new Lance(FRAN,400));
        LEILAO.propoe(new Lance(USUARIO,500));
        LEILAO.propoe(new Lance(FRAN,600));
        LEILAO.propoe(new Lance(USUARIO,700));
        LEILAO.propoe(new Lance(FRAN,800));
        LEILAO.propoe(new Lance(USUARIO,900));
        LEILAO.propoe(new Lance(FRAN,1000));
        LEILAO.propoe(new Lance(USUARIO, 1100));
    }
}