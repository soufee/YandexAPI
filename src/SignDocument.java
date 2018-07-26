import java.io.*;
import java.util.Base64;
import java.util.Date;

public class SignDocument {
    double stampPortraitPercent = 28.57;
    double stampLandscapePercent = 20.2;
    double sideMargin = 10; //----------------
    double bottomMargin = 20;
    double qualificationsMargin = 1;
    String qualificationsText = "Тестовые оговорки";                  //Param.Qualifications
    String dateFormat = "dd.MM.yyyy";
    String fontPath = "arial.ttf";
    String signDateStr = "19850429";                         //Param.SignDate
    int qualificationsFontSize = 8;
    int dateFontSize = 14;
    boolean isNeedBackground = false;
    String srcDocumentStream;                   //.pyAttachStream +++++++++
    String trgDocumentStream;                   //исходящий документ
    String stampStream;                         //байтовый код печати +++++++++++
    String colorFont = "3059b4";
    String iccProfileStream;                    //ICCProfile.pyFileSource
    boolean isNeedQualificationsBorder = true;

    String fileInput = "eCertificate.pdf";


    public static void main(String[] args) throws Exception {
        new SignDocument().SignPdfDoc();
    }

    void SignPdfDoc() throws Exception {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.ByteArrayOutputStream pdfaos = new java.io.ByteArrayOutputStream();
        com.itextpdf.text.pdf.PdfStamper stamper = null;


        oLog.debug("Init generateing document with stamp. List of input variables:");
        oLog.debug("stampPortraitPercent=" + stampPortraitPercent);
        oLog.debug("stampLandscapePercent=" + stampLandscapePercent);
        oLog.debug("sideMargin=" + sideMargin);
        oLog.debug("bottomMargin=" + bottomMargin);
        oLog.debug("qualificationsText=" + qualificationsText);
        oLog.debug("qualificationsFontSize=" + qualificationsFontSize);
        oLog.debug("qualificationsMargin=" + qualificationsMargin);
        oLog.debug("dateFontSize=" + dateFontSize);
        oLog.debug("dateFormat=" + dateFormat);
        oLog.debug("fontPath=" + fontPath);
        oLog.debug("isNeedBackground=" + isNeedBackground);
        oLog.debug("signDateStr=" + signDateStr);

        boolean isNeedRotate = false;
        java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
        try {
            oLog.debug("Start generating document");
            File pdfFile = new File(fileInput);
            File stamp = new File("base64.png");
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] buffer;
            try (FileInputStream fin = new FileInputStream(pdfFile)) {
                buffer = new byte[fin.available()];
                fin.read(buffer, 0, buffer.length);
            }
            srcDocumentStream = encoder.encodeToString(buffer);
            try (FileInputStream fin = new FileInputStream(stamp)) {
                buffer = new byte[fin.available()];
                fin.read(buffer, 0, buffer.length);
            }
            stampStream = encoder.encodeToString(buffer);

            try (FileInputStream fin = new FileInputStream("srgb_cs_profile.icm")) {
                buffer = new byte[fin.available()];
                fin.read(buffer, 0, buffer.length);
            }
            iccProfileStream = encoder.encodeToString(buffer);


            byte[] srcDocumentDecoded = decoder.decode(srcDocumentStream);
            byte[] stampDecoded = decoder.decode(stampStream);

            com.itextpdf.text.pdf.PdfReader reader = new com.itextpdf.text.pdf.PdfReader(srcDocumentDecoded);
            stamper = new com.itextpdf.text.pdf.PdfStamper(reader, baos);

            java.text.SimpleDateFormat pegaDateFormatter = new java.text.SimpleDateFormat("yyyyMMdd");
            java.text.SimpleDateFormat dateFormatter = new java.text.SimpleDateFormat(dateFormat);
            Date signDate = pegaDateFormatter.parse(signDateStr);

            com.itextpdf.text.pdf.BaseFont bf = com.itextpdf.text.pdf.BaseFont.createFont(fontPath, com.itextpdf.text.pdf.BaseFont.IDENTITY_H, true);
            com.itextpdf.text.Font dateFont = new com.itextpdf.text.Font(bf, dateFontSize);
            dateFont.setColor(new com.itextpdf.text.BaseColor(Integer.decode("0x" + colorFont)));

            String dateStr = dateFormatter.format(signDate);
            String offrerUniqueID = "OfferUniqueID";

            com.itextpdf.text.Paragraph dateText = new com.itextpdf.text.Paragraph(dateStr, dateFont);
              com.itextpdf.text.Paragraph offerUniqeIDText = new com.itextpdf.text.Paragraph(offrerUniqueID, dateFont);
            float offsetByDateSize = bf.getWidthPoint(dateStr, dateFontSize);
            float offsetByOfferUniqueIDSize = bf.getWidthPoint(offrerUniqueID, dateFontSize);

            for (int i = 1; i <= reader.getNumberOfPages(); ++i) {
                oLog.debug("Start stamping on page: " + i);
                com.itextpdf.text.pdf.PdfContentByte content = stamper.getOverContent(i);
                content.saveState();

                com.itextpdf.text.Rectangle pageSize = reader.getPageSize(i);
                float pageWidth = pageSize.getWidth();
                float pageHeight = pageSize.getHeight();
                oLog.debug("pageWidth=" + pageWidth + ", pageHeight=" + pageHeight);
                float stampPercent;
                if (pageWidth < pageHeight) {
                    stampPercent = (float) stampPortraitPercent;
                } else {
                    stampPercent = (float) stampLandscapePercent;
                    isNeedRotate = true;
                }
                com.itextpdf.text.Image stampImage = com.itextpdf.text.Image.getInstance(stampDecoded);
                double scaledStampWidth = pageWidth * stampPercent / 100.;
                double scaledStampHeight = stampImage.getHeight() * scaledStampWidth / stampImage.getWidth();
                float stampPositionX = pageWidth - (float) scaledStampWidth - (float) sideMargin;
                oLog.debug("scaledStampWidth=" + scaledStampWidth);
                oLog.debug("scaledStampHeight=" + scaledStampHeight);
                oLog.debug("stampPositionX=" + stampPositionX);
                oLog.debug("stampPositionY=" + bottomMargin);

                stampImage.scaleAbsolute((float) scaledStampWidth, (float) scaledStampHeight);
                stampImage.setAbsolutePosition(stampPositionX, (float) bottomMargin);

                com.itextpdf.text.pdf.ColumnText.showTextAligned(content, com.itextpdf.text.Element.ALIGN_LEFT, dateText, (float) sideMargin, (float) bottomMargin, 0);
                com.itextpdf.text.pdf.ColumnText.showTextAligned(content, com.itextpdf.text.Element.ALIGN_LEFT, offerUniqeIDText, (float) sideMargin, (float) bottomMargin-12, 0);
                content.addImage(stampImage);

                if (qualificationsText != null && !"".equals(qualificationsText)) {
                    oLog.debug("Start insert qualifications on page: " + i);
                    com.itextpdf.text.Font qualificationsFont = new com.itextpdf.text.Font(bf, qualificationsFontSize);
                    qualificationsFont.setColor(new com.itextpdf.text.BaseColor(Integer.decode("0x" + colorFont)));
                    float resultOffsetQualificationsX = (float) (sideMargin + offsetByDateSize + qualificationsMargin);
                    float qualificationsWidth = (float) (stampPositionX - qualificationsMargin - resultOffsetQualificationsX);
                    com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(1);
                    table.setTotalWidth(qualificationsWidth);
                    String[] qualificationArray = qualificationsText.split(";");
                    for (String qualification : qualificationArray) {
                        oLog.debug("add qualification: " + qualification);
                        com.itextpdf.text.Paragraph qualifications = new com.itextpdf.text.Paragraph(qualification.trim(), qualificationsFont);
                        com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell();
                        cell.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_BOTTOM);
                        cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED_ALL);
                        cell.addElement(qualifications);
                        cell.setPaddingTop((float) qualificationsMargin);
                        cell.setBorderColor(new com.itextpdf.text.BaseColor(Integer.decode("0x" + colorFont)));
                        if (!isNeedQualificationsBorder) {
                            cell.setBorder(0);
                        }
                        if (isNeedBackground) {
                            cell.setBackgroundColor(com.itextpdf.text.BaseColor.WHITE);
                        }
                        table.addCell(cell);
                    }

                    float resultOffsetQualificationsY = (float) (bottomMargin + table.calculateHeights());
                    oLog.debug("resultOffsetQualificationsX=" + resultOffsetQualificationsX);
                    oLog.debug("resultOffsetQualificationsY=" + resultOffsetQualificationsY);
                    oLog.debug("qualificationsWidth=" + qualificationsWidth);
                    table.writeSelectedRows(0, -1, resultOffsetQualificationsX, resultOffsetQualificationsY, content);
                    oLog.debug("Finish insert qualifications on page: " + i);
                }

                content.restoreState();
                oLog.debug("End stamping on page: " + i);
            }
        } catch (java.lang.Exception e) {
            oLog.error("exception stamp", e);
            throw new Exception(e);
        } finally {
            if (stamper != null) {
                try {
                    stamper.close();
                } catch (java.lang.Exception e) {
                    throw new java.lang.RuntimeException(e);
                }
            }
        }

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        if (isNeedRotate) {
            document.setPageSize(com.itextpdf.text.PageSize.A4.rotate());
        }
        com.itextpdf.text.pdf.PdfReader reader = null;
        try {
            byte[] iccProfileDecoded = decoder.decode(iccProfileStream);
            com.itextpdf.text.pdf.PdfAWriter writer = com.itextpdf.text.pdf.PdfAWriter.getInstance(document, pdfaos, com.itextpdf.text.pdf.PdfAConformanceLevel.PDF_A_2B);
            document.addCreationDate();

            writer.setTagged();
            writer.createXmpMetadata();
            writer.setFullCompression();

            document.open();
            com.itextpdf.text.pdf.PdfContentByte cb = writer.getDirectContent();

            reader = new com.itextpdf.text.pdf.PdfReader(baos.toByteArray());
            com.itextpdf.text.pdf.PdfImportedPage page;
            int pageCount = reader.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                document.newPage();
                page = writer.getImportedPage(reader, i + 1);
                cb.addTemplate(page, 0, 0);
            }
            com.itextpdf.text.pdf.ICC_Profile icc_profile = com.itextpdf.text.pdf.ICC_Profile.getInstance(iccProfileDecoded);
            writer.setOutputIntents("Custom", "", "http://www.color.org",
                    "sRGB IEC61966-2.1", icc_profile);

        } catch (com.itextpdf.text.DocumentException de) {
            oLog.error("exception creating pdf/a", de);
            throw new Exception(de);
        } catch (java.io.IOException ioe) {
            oLog.error("exception creating pdf/a", ioe);
            throw new Exception(ioe);
        } finally {
            if (document != null) {
                document.close();
            }
        }

        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        trgDocumentStream = encoder.encodeToString(pdfaos.toByteArray());
        Base64.Decoder dec = Base64.getDecoder();
        byte[] output = dec.decode(trgDocumentStream);

        OutputStream ois = new FileOutputStream("2.pdf");
        ois.write(output);
        ois.flush();
        ois.close();

    }

    static class oLog {
        public static void debug(String s) {
            System.out.println(s);
        }

        public static void error(String message, Exception e) {
            System.out.println(message);
        }
    }

}
