// looks like Nashorn (JVM8) and Rhino (JVM7) have some syntax differences
// let's try to keep compatibility as much as possible

if (jvmVersion === '8') {
    var scope = new JavaImporter(Packages.net.dstc.mochatemplate);
    Person = scope.Person;
} else {
    importClass(Packages.net.dstc.mochatemplate.Person)
}
