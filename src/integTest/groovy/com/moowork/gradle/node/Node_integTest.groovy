package com.moowork.gradle.node

import com.moowork.gradle.AbstractIntegTest

class Node_integTest
    extends AbstractIntegTest
{
    def 'exec simple node program'()
    {
        given:
        writeBuild( '''
            apply plugin: 'com.moowork.node'

            node {
                version = "0.10.33"
                npmVersion = "2.1.6"
                download = true
                workDir = file('build/node')
            }

            task simple(type: NodeTask) {
                script = file('simple.js')
                args = []
            }
        ''' )
        writeEmptyPackageJson()
        writeFile( 'simple.js', """
            console.log("Hello World");
        """ )

        when:
        def result = runTasksSuccessfully( 'simple' )

        then:
        result.wasExecuted( 'simple' )
    }

    def 'check environment settings'()
    {
        given:
        writeBuild( '''
            apply plugin: 'com.moowork.node'

            node {
                version = "0.10.33"
                npmVersion = "2.1.6"
                download = true
                workDir = file('build/node')
            }

            task simple(type: NodeTask) {
                script = file('simple.js')
                args = []
                environment = ['MYENV': 'value']
            }
        ''' )
        writeEmptyPackageJson()
        writeFile( 'simple.js', """
            if (process.env.MYENV == 'value') {
                console.log("Hello MYENV=" + process.env.MYENV);
            } else {
                throw "Environment MYENV should be visible";
            }
        """ )

        when:
        def result = runTasksSuccessfully( 'simple' )

        then:
        result.wasExecuted( 'simple' )
    }
}
