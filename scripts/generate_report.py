'''
Create html report from survey xml files saved by ffem Collect
'''
from decimal import Decimal
from lxml import etree, objectify
import re
import glob, os
import datetime

def formatValue(value):
    formatted = format(float(str(value).replace('>','')), '.2f')
    if str(value).find('>') > -1:
        return '> ' + formatted
    else:
        return formatted

class HtmlReport(object):
    """"""
    #----------------------------------------------------------------------
    def __init__(self, xml_file, html_file):
        self.xml_file = xml_file
        self.html_file = html_file
 
        self.xml_obj = self.getXMLObject()
 
    #----------------------------------------------------------------------
    def createHtml(self):
        global output

        xml = self.xml_obj
 
        with open(self.html_file) as f:
            html = f.read()

        farmer = str(xml.Farmer_name)
        geo = str(xml.Geolocation).split(' ')

        date = datetime.datetime.strptime(str(xml.Date), '%Y-%m-%d').strftime('%d %B %Y')
        state = str(xml.State)
        if (state == '27'):
            state = 'Maharashtra'
        else:
            print 'error: ' + state
        
        district = str(xml.District)
        if (district == '525'):
            district = 'Osmanabad'
        else:
            print 'error: ' + district

        crop = str(xml.Crop_004)
        if (crop == '13'):
            crop = 'Soyabean'
        else:
            print 'error: ' + crop

        output += html % (date,
        farmer.title(), xml.Phone_number, xml.Village_name, state, district, xml.Sample_number, crop, 
        geo[0], geo[1], geo[2], geo[3], 
        formatValue(xml.Available_Nitrogen), formatValue(xml.Available_Phosphorous), formatValue(xml.Available_Potassium), xml.pH,
        xml.Comb1Fert1, formatValue(xml.Comb1Value1), xml.Comb1Fert2, formatValue(xml.Comb1Value2), xml.Comb1Fert3, formatValue(xml.Comb1Value3),
        xml.Comb2Fert1, formatValue(xml.Comb2Value1), xml.Comb2Fert2, formatValue(xml.Comb2Value2), xml.Comb2Fert3, formatValue(xml.Comb2Value3), 
        date)

    #----------------------------------------------------------------------
    def getXMLObject(self):
        with open(self.xml_file) as f:
            xml = f.read()
        rxml = re.sub(r"<group.*>", '', xml)
        rxml = re.sub(r"</group.*>", '', rxml)
        return objectify.fromstring(rxml)
  
#----------------------------------------------------------------------
if __name__ == "__main__":
    html = "template.html"
    output = '<style>table {width: 100%} td {padding-bottom: 10px} .valueCell {text-align: right}</style><body style="margin:0;font-family:Calibri,sans-serif;font-size:14px;color:#000;">'
    add_break = False

    for file in glob.glob("xml/*.xml"):
        print file
        doc = HtmlReport(file, html)
        if add_break:
            output += '<p style="page-break-before: always"></p>'
        doc.createHtml()
        add_break = True

    output += '</body>'
    file = open("report.html","w")
    file.write(output)
    file.close()
