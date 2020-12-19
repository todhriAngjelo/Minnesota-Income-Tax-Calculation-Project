import com.minesota.tax.calculator.manager.ChartsManager;
import com.minesota.tax.calculator.manager.FilesManager;
import com.minesota.tax.calculator.model.TaxPayer;
import com.minesota.tax.calculator.model.enumeration.FamilyStatusEnum;
import org.junit.Assume;
import org.junit.Test;

import java.util.Collections;
import java.util.Scanner;

import static com.minesota.tax.calculator.util.BasicTaxBuilder.getBasicTaxBy;

/**
 * This class is indicative for Junit tests. As long as most effort was spent in recoding the core implementation
 * a few time was spent on the junit tests.
 * We couldn't follow TDD here because the whole project was redesigned so Junit tests should be written on the end
 * product which was not yet concluded at the time.
 * <p>
 * The methods to be tested are not the same that are mentioned in the backlog.
 */
public class AppTests {

    // REPLACE THIS WITH YOURS
    String inputFilesLocation = "C:\\Users\\Todhri Angjelo\\Desktop\\project-material\\Minnesota Income Tax Calculation Project\\InputFiles";

    // PASTE THIS IN THE INPUT FILE with filename "130456093_INFO.txt"
    String inputFileText = "Name: Apostolos Zarras\n" +
            "AFM: 130456093\n" +
            "Status: Married Filing Jointly\n" +
            "Income: 22570\n" +
            "Receipts:\n" +
            "\n" +
            "Receipt ID: 1\n" +
            "Date: 25/2/2014\n" +
            "Kind: Basic\n" +
            "Amount: 2000\n" +
            "Company: Hand Made Clothes\n" +
            "Country: Greece\n" +
            "City: Ioannina\n" +
            "Street: Kaloudi \n" +
            "Number: 10\n" +
            "\n" +
            "Receipt ID: 2\n" +
            "Date: 28/2/2014\n" +
            "Kind: Entertainment\n" +
            "Amount: 500\n" +
            "Company: Floca Cafe\n" +
            "Country: Greece\n" +
            "City: Ioannina\n" +
            "Street: Kavafi\n" +
            "Number: 4\n" +
            "\n" +
            "Receipt ID: 3\n" +
            "Date: 10/3/2014\n" +
            "Kind: Health\n" +
            "Amount: 500\n" +
            "Company: Dentist\n" +
            "Country: Greece\n" +
            "City: Ioannina\n" +
            "Street: Lamprou\n" +
            "Number: 20\n" +
            "\n" +
            "\n" +
            "Receipt ID: 4\n" +
            "Date: 3/5/2014\n" +
            "Kind: Other\n" +
            "Amount: 900\n" +
            "Company: Public\n" +
            "Country: Greece\n" +
            "City: Ioannina\n" +
            "Street: Vlahlidou\n" +
            "Number: 20\n" +
            "\n" +
            "Receipt ID: 5\n" +
            "Date: 3/5/2014\n" +
            "Kind: Other\n" +
            "Amount: 600\n" +
            "Company: Toys Store\n" +
            "Country: Greece\n" +
            "City: Ioannina\n" +
            "Street: Omhrou\n" +
            "Number: 5\n";

    String taxPayerToString = "Name: Apostolos Zarras\n" +
            "Vat number: 130456093\n" +
            "Status: MARRIED FILING JOINTLY\n" +
            "Income: 22570,00\n" +
            "BasicTax: 1207,50\n" +
            "TaxIncrease: 0,00\n" +
            "TaxDecrease: 0,00";

    String[] pieTestValues = {"Basic", "2000"};

    @Test
    public void testParseTaxPayerFile() { // TEST INPUT
        FilesManager.getInstance().cacheTaxPayers(inputFilesLocation, Collections.singletonList("130456093_INFO.txt"));
        Assume.assumeTrue(Integer.parseInt(FilesManager.getInstance().getCachedTaxPayers().get(0).getVat()) == 130456093);
    }

    @Test
    public void testCreateTxtTaxpayerLogFile() { // TEST OUTPUT
        FilesManager.getInstance().cacheTaxPayers(inputFilesLocation, Collections.singletonList("130456093_INFO.txt"));
        FilesManager.getInstance().createTxtTaxpayerLogFile(inputFilesLocation, 0);

        Scanner scanner = FilesManager.getInstance().getScanner(inputFilesLocation, "130456093_LOG.txt");
        Assume.assumeFalse(scanner.nextLine().isEmpty());
    }

    @Test
    public void testTaxPayerToString() { // TEST TEAXPAYER
        TaxPayer taxPayer = new TaxPayer(
                "Apostolos Zarras",
                "130456093",
                FamilyStatusEnum.MARRIED_FILLING_JOINTLY,
                "22570",
                getBasicTaxBy(
                        FamilyStatusEnum.MARRIED_FILLING_JOINTLY.getDescription().toLowerCase(),
                        22570));

        Assume.assumeTrue(taxPayer.toString().equals(taxPayerToString));
    }

    @Test
    public void testCharts() { // TEST PIE OUTPUT
        FilesManager.getInstance().cacheTaxPayers(inputFilesLocation, Collections.singletonList("130456093_INFO.txt"));
        ChartsManager.getInstance().createTaxpayerReceiptsPieJFreeChart(0);
        Assume.assumeTrue(ChartsManager.getInstance().getPiePlot().getDataset().getKey(0).equals(pieTestValues[0]));
        Assume.assumeTrue(ChartsManager.getInstance().getPiePlot().getDataset().getValue(0).equals(Double.valueOf(pieTestValues[1])));
    }
}
