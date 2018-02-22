package org.reactome.server.tools.analysis.exporter.playground.util;

import org.junit.Test;
import org.reactome.server.graph.service.DatabaseObjectService;
import org.reactome.server.graph.service.GeneralService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfUtilsTest {


    private static GeneralService genericService;
    private static DatabaseObjectService databaseObjectService;

    static {
        final ApplicationContext context = new AnnotationConfigApplicationContext(GraphCoreConfig.class);
        genericService = context.getBean(GeneralService.class);
        databaseObjectService = context.getBean(DatabaseObjectService.class);
    }

    @Test
    public void getText() throws IOException {
//        String[] LITERATURE = PdfUtils.getText("src/main/resources/texts/references.txt");
//        for (String string : LITERATURE) {
//            System.out.println(string);
//            System.out.println(string.split("\t").length);
//        }
//        HashMap<String, StringBuilder> strings = new HashMap<>();
//        strings.put("123", new StringBuilder("123"));
//        System.out.println(strings.get("123"));
//        strings.get("123").append("qwe");
//        Identifier identifier01 = new Identifier();
//        identifier01.setId("123qwe");
//        Identifier identifier02 = new Identifier();
//        identifier02.setId("123qw");
//        Set<Identifier> identifiers = new HashSet<>();
//        System.out.println(identifiers.add(identifier01));
//        System.out.println(identifiers.add(identifier02));
//
//        PdfDocument document = new PdfDocument(new PdfWriter("src/test/resources/demo.pdf"));
//        Document doc = new Document(document);
//        Table table = new Table(new UnitValue[4]);
//        table.setWidth(UnitValue.createPercentValue(100));
//        Cell cell = new Cell();
////        cell.setNextRenderer(new CellTextRenderer(cell, ""));
//        table.addCell(cell);
//        table.addCell(new Cell(1, 1).add(new Paragraph("01").setAction(PdfAction.createGoTo("345"))));
//        table.addCell(new Cell(1, 1).add(new Paragraph("02").setAction(PdfAction.createGoTo("123"))));
//        table.addCell(new Cell(1, 1).add(new Paragraph("123")));
//        table.addCell(new Cell(1, 1).add(new Paragraph("123")));
//        table.addCell(new Cell(1, 1).add(new Paragraph("123")));
//        table.addCell(new Cell(1, 1).add(new Paragraph(new Text("01").setDestination("345"))
//                .add(new Text("02").setDestination("123"))));
//        doc.add(table);
//        doc.close();
//        document.close();
//        System.out.println("start"+String.valueOf('\u0000')+"end");
//        String html = "MET is a receptor tyrosine kinase (RTK) (Cooper et al. 1984, Park et al. 1984) activated by binding to its ligand, Hepatocyte growth factor/Scatter factor (HGF/SF) (Bottaro et al. 1991, Naldini et al. 1991). Similar to other related RTKs, such as EGFR, ligand binding induces MET dimerization and trans-autophosphorylation, resulting in the active MET receptor complex (Ferracini et al. 1991, Longati et al. 1994, Rodrigues and Park 1994, Kirchhofer et al. 2004, Stamos et al. 2004, Hays and Watowich 2004). Phosphorylated tyrosines in the cytoplasmic tail of MET serve as docking sites for binding of adapter proteins, such as GRB2, SHC1 and GAB1, which trigger signal transduction cascades that activate PI3K/AKT, RAS, STAT3, PTK2, RAC1 and RAP1 signaling (Ponzetto et al. 1994, Pelicci et al. 1995, Weidner et al. 1995, Besser et al. 1997, Shen and Novak 1997, Beviglia and Kramer 1999, Rodrigues et al. 2000, Sakkab et al. 2000, Schaeper et al. 2000, Lamorte et al. 2002, Wang et al. 2002, Chen and Chen 2006, Palamidessi et al. 2008, Chen et al. 2011, Murray et al. 2014).<br>Activation of PLC gamma 1 (PLCG1) signaling by MET remains unclear. It has been reported that PLCG1 can bind to MET directly (Ponzetto et al. 1994) or be recruited by phosphorylated GAB1 (Gual et al. 2000). Tyrosine residue Y307 of GAB1 that serves as docking sites for PLCG1 may be phosphorylated either by activated MET (Watanabe et al. 2006) or SRC (Chan et al. 2010). Another PCLG1 docking site on GAB1, tyrosine residue Y373, was reported as the SRC target, while the kinase for the main PLCG1 docking site, Y407 of GAB1, is not known (Chan et al. 2010).<br>Signaling by MET promotes cell growth, cell survival and motility, which are essential for embryonic development (Weidner et al. 1993, Schmidt et al. 1995, Uehara et al. 1995, Bladt et al. 1995, Maina et al. 1997, Maina et al. 2001, Helmbacher et al. 2003) and tissue regeneration (Huh et al. 2004, Borowiak et al. 2004, Liu 2004, Chmielowiec et al. 2007). MET signaling is frequently aberrantly activated in cancer, through MET overexpression or activating MET mutations (Schmidt et al. 1997, Pennacchietti et al. 2003, Smolen et al. 2006, Bertotti et al. 2009).<br>Considerable progress has recently been made in the development of HGF-MET inhibitors in cancer therapy. These include inhibitors of HGF activators, HGF inhibitors and MET antagonists, which are protein therapeutics that act outside the cell. Kinase inhibitors function inside the cell and have constituted the largest effort towards MET-based therapeutics (Gherardi et al. 2012).<br>Pathogenic bacteria of the species Listeria monocytogenes, exploit MET receptor as an entryway to host cells (Shen et al. 2000, Veiga and Cossart 2005, Neimann et al. 2007).<br>For review of MET signaling, please refer to Birchmeier et al. 2003, Trusolino et al. 2010, Gherardi et al. 2012, Petrini 2015.";
//        Document document = Jsoup.parse(html);
//        System.out.println(document.body().text());

        System.out.println(databaseObjectService.findById(48887).getDisplayName());
    }
}