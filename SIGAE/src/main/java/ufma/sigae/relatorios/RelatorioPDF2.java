package ufma.sigae.relatorios;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import ufma.sigae.entidades.Ocupacao;
import ufma.sigae.entidades.Egresso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Cria e salva um relatório em pdf do tipo 2
 * <p>[Nome do Egresso] [Posição | Salário | Descrição]</p>
 * Ordenado por nome e filtrado por um intervalo de ano de conclusão dos egressos
 */
public class RelatorioPDF2 implements Relatorio {
    /** Documento com o relatorio pdf para ser gerado */
    private final Document documentoPDF;
    /** Lista de egressos já filtrado para gera o relatório */
    private final List<Egresso> egressos;

    /**
     * Inicializa os dados para gerar um relatório em pdf do tipo 2
     * @param egressos lista de egressos já filtrado para gera o relatório
     * @param curso curso do coordenador para determinar a pasta dos relatórios
     * @throws IOException se não for possível acessar as pastas ou o arquivo
     */
    public RelatorioPDF2(List<Egresso> egressos, String curso) throws IOException {
        this.egressos = egressos;
        this.documentoPDF = new Document();
        String caminho = "files/relatorios/"+curso+"/";
        File pastas = new File(caminho);
        if (!pastas.exists()) {
            boolean criou = pastas.mkdirs();
            if (!criou) {
                throw new IOException("Não foi possível criar as pastas");
            }
        }
        caminho = caminho+"Relatorio2.pdf";
        PdfWriter.getInstance(this.documentoPDF, new FileOutputStream(caminho));
        this.documentoPDF.open();
    }

    /**
     * Gera o cabeçalho do relatório
     */
    public void gerarCabecalho() {
        Paragraph paragrafoTitulo = new Paragraph();
        paragrafoTitulo.setAlignment(1);
        paragrafoTitulo.add(new Chunk("RELATORIO DE EGRESSOS 2", new Font(1, 24.0F)));
        this.documentoPDF.add(paragrafoTitulo);
        this.documentoPDF.add(new Paragraph("\n"));
    }

    /**
     * Gera o corpo do relatório
     */
    public void gerarCorpo() {
        for (Egresso egresso : egressos) {
            Paragraph paragrafoNomeEOcupacao = new Paragraph();
            paragrafoNomeEOcupacao.setAlignment(Element.ALIGN_LEFT);
            paragrafoNomeEOcupacao.add(new Paragraph("\nEGRESSO: " + egresso.getNome() + "\n"));
            this.documentoPDF.add(new Paragraph(""));
            for (Ocupacao ocupacao:egresso.getOcupacoes()) {
                Chunk texto = new Chunk();
                texto.append("CARGO: " + ocupacao.getCargo() + "\n");
                texto.append("SALARIO: R$ "+ ocupacao.getSalario() + "\n");
                texto.append("DESCRIÇÃO: " + ocupacao.getDescricao() + "\n");
                texto.append("-".repeat(130)+"\n");
                paragrafoNomeEOcupacao.add(texto);
            }
            this.documentoPDF.add(paragrafoNomeEOcupacao);
        }
    }

    /**
     * Salva e gera o arquivo pdf
     */
    public void imprimir() {
        if (this.documentoPDF != null && this.documentoPDF.isOpen()) {
            this.documentoPDF.close();
        }
    }
}
