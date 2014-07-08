package com.sparsity.sparksee.groovy;

class MetaSparksee {

    public static void ini() {
        com.sparsity.sparksee.gdb.Sparksee.metaClass.static.create = { gdb, alias ->
            def sg = new Graph()
            sg.config = new com.sparsity.sparksee.gdb.SparkseeConfig()
            sg.sparksee = new com.sparsity.sparksee.gdb.Sparksee(sg.config)
            sg.db = sg.sparksee.create(gdb, alias)
            sg.sess = sg.db.newSession()
            sg.graph = sg.sess.getGraph()
            sg
        }

        com.sparsity.sparksee.gdb.Sparksee.metaClass.static.use = { gdb ->
            def sg = new Graph()
            sg.config = new com.sparsity.sparksee.gdb.SparkseeConfig()
            sg.sparksee = new com.sparsity.sparksee.gdb.Sparksee(sg.config)
            sg.db = sg.sparksee.open(gdb, false)
            sg.sess = sg.db.newSession()
            sg.graph = sg.sess.getGraph()
            sg
        }
    }
}