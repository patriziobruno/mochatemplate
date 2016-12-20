# mochatemplate
[![license](https://img.shields.io/github/license/patriziobruno/mochatemplate.svg)](https://raw.githubusercontent.com/patriziobruno/mochatemplate/master/LICENSE)
[![Build Status](https://travis-ci.org/patriziobruno/mochatemplate.svg?branch=master)](https://travis-ci.org/patriziobruno/mochatemplate)
[![Coverage Status](https://coveralls.io/repos/github/patriziobruno/mochatemplate/badge.svg?branch=master)](https://coveralls.io/github/patriziobruno/mochatemplate?branch=master)
[![Coverity Scan Build Status](https://scan.coverity.com/projects/11225/badge.svg?flat=1)](https://scan.coverity.com/projects/patriziobruno/mochatemplate)

A template library inspired by Adobe Sightly (https://docs.adobe.com/ddc/en/gems/introduction-to-sightly.html).

It makes use of:
 - jsoup (https://jsoup.org/) for HTML parsing
 - Reflections (https://github.com/ronmamo/reflections) for runtime loading of template-nodes interpreters
 - JDK native script engine (Nashorn for JDK8, Rhino for JDK7)

To use the library in your application you can either instantiate MochaTemplateEngine from your servlet or add net.desertconsulting.mochatemplate.servlet.MainServlet to your web.xml.

###Test
To run unit tests:
```bash
$ mvn test
```

To run a jetty instance serving the included example:
```bash
$ mvn jetty:run
```

###Example template
```html
<html>
  <head>
    <script type="server/javascript">
    var Person = importClass(Packages.examples.Person);
    var query = request.getParameter('search');
    </script>
  </head>
  <body>
    <div data-for-person="${Person.lookupByName(query)}">
      <p>${person.FullName}</p>
    </div>
  </body>
</html>
```
