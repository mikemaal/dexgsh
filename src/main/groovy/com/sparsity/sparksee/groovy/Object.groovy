package com.sparsity.sparksee.groovy;

abstract class Object {
    
    // Read-only properties
    
    final Graph sg
    
    final long oid
    
    final int typeId
    
    final String type 
    
    public Object(Graph sg, long oid) {
        this.sg = sg
        this.oid = oid
        this.typeId = sg.graph.getObjectType(oid)
        com.sparsity.sparksee.gdb.Type tdata = sg.graph.getType(typeId)
        this.type = tdata.getName();
    }
    
    abstract String toString()
    
    def getAttrs() {
        def ret = [:]
        com.sparsity.sparksee.gdb.Value v = new com.sparsity.sparksee.gdb.Value()
        com.sparsity.sparksee.gdb.AttributeList attrs = sg.graph.getAttributes(oid)
        attrs.each { attr ->
            com.sparsity.sparksee.gdb.Attribute adata = sg.graph.getAttribute(attr)
            sg.graph.getAttribute(oid, attr, v)
            ret[adata.getName()] = v.toString()
        }
        ret
    }

    // setter    
    def propertyMissing(String name, value) {
        def attr = sg.graph.findAttribute(typeId, name)
        if (attr == com.sparsity.sparksee.gdb.Attribute.InvalidAttribute) {
            attr = sg.graph.newAttribute(typeId, name, 
                com.sparsity.sparksee.gdb.DataType.String, 
                com.sparsity.sparksee.gdb.AttributeKind.Indexed);
        }
        com.sparsity.sparksee.gdb.Value v = new com.sparsity.sparksee.gdb.Value()
        sg.graph.setAttribute(oid, attr, v.setString(value.toString()));
    }
    
    // getter
    def propertyMissing(String name) {
        def attr = sg.graph.findAttribute(typeId, name)
        if (attr == com.sparsity.sparksee.gdb.Attribute.InvalidAttribute) {
            throw new IllegalArgumentException("Unnexisting attribute $name")
        }
        com.sparsity.sparksee.gdb.Value v = new com.sparsity.sparksee.gdb.Value()
        sg.graph.getAttribute(oid, attr, v)
        v.toString();
    }
}
