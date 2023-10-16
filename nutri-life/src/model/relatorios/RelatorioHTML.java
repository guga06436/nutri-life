package model.relatorios;
import handlers.OptionHandler;

public class RelatorioHTML extends Relatorio
{
    @Override
    public void gerarRelatorio()
    {
        OptionHandler.showMessage("Gerando relatório em HTML");
    }
}