Ext.define('Ext.ux.form.MultiFile', {
    extend: 'Ext.form.field.File',
    alias: 'widget.multifilefield',
 
    initComponent: function () {
        var me = this;
 
        me.on('render', function () {
            me.fileInputEl.set({ 
            	multiple: true,
            	style:'border:0;position:absolute;cursor:pointer;top:-2px;right:-2px;opacity:0;'
            });
        });
        
        me.callParent(arguments);
    },
 
    onFileChange: function (button, e, value) {
        this.duringFileSelect = true;
 
        var me = this,
            upload = me.fileInputEl.dom,
            files = upload.files,
            names = [];
 
        if (files) {
            for (var i = 0; i < files.length; i++)
                names.push(files[i].name);
            value = names.join(', ');
        }
 
        Ext.form.field.File.superclass.setValue.call(this, value);
 
        delete this.duringFileSelect;
    }
});