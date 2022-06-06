params="-Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Dswagger2markup.skip=true -Dasciidoctor.skip=true"
./mvnw release:rollback -Darguments="${params}"}