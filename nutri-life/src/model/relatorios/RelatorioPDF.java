package model.relatorios;
import handlers.OptionHandler;

public class RelatorioPDF extends Relatorio
{
    @Override
    public void gerarRelatorio()
    {
        OptionHandler.showMessage("Gerando relatório em PDF");
    }
}