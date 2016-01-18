import sys
import os

import test1

from org.jsoup import Jsoup
from com.pixshow.framework.utils import HttpUtility

url = "http://en.wikipedia.org/";

print test1.workDir()

html = HttpUtility.get(url);
doc = Jsoup.parse(html)

html = doc.select('#mp-itn b a').toString()

appContext.get('testService').save(html)
