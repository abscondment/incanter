(ns 
#^{:doc "This library currently has only a single function, save-pdf, which saves
  charts as a PDF file. To build this namespace make sure the you have the iText
  library (http://itextpdf.com/) as a declared dependency in your pom.xml or 
  project.clj file:
  [com.lowagie/itext \"1.4\"] "}
 
  incanter.pdf
  (:use (incanter charts))
  (:import (com.lowagie.text Document DocumentException Rectangle)
	   (com.lowagie.text.pdf DefaultFontMapper FontMapper PdfContentByte
				 PdfTemplate PdfWriter)
	   (java.awt Graphics2D) 
	   (java.awt.geom Rectangle2D)
	   (java.io BufferedOutputStream File FileOutputStream OutputStream)	 
	   (java.text SimpleDateFormat)))


(defn save-pdf
" Save a chart object as a pdf document.

  Arguments:
    chart
    filename

  Options:
    :width (default 500)
    :height (defualt 400)

  Examples:
    
    (use '(incanter core charts))
    (save-pdf (function-plot sin -4 4) \"./pdf-chart.pdf\")
    

"
  ([chart filename & options] 
     (let [opts (when options (apply assoc {} options))
	   width (or (:width opts) 500)
	   height (or (:height opts) 400)
	   pagesize (Rectangle. width height)
	   document (Document. pagesize 50 50 50 50)
	   out (BufferedOutputStream. (FileOutputStream. filename))
	   writer (PdfWriter/getInstance document out)
	   _ (.open document)
	   cb (.getDirectContent writer)
	   tp (.createTemplate cb width height)
	   mapper (DefaultFontMapper.)
	   g2 (.createGraphics tp width height mapper)
	   r2D (new java.awt.geom.Rectangle2D$Double 0 0 width height)] 
       (do
	 (.draw chart g2 r2D)
	 (.dispose g2)
	 (.addTemplate cb tp 0 0)
	 (.close document)
	 (.close out)))))


