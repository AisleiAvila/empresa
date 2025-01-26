# Use uma imagem base do Ubuntu
FROM ubuntu:22.04

# Instale o OpenJDK 22
RUN apt-get update && apt-get install -y openjdk-22-jdk

# Defina o diretório de trabalho
WORKDIR /app

# Copie o arquivo JAR da aplicação
COPY target/empresa-0.0.1-SNAPSHOT.jar /app/app.jar

# Exponha a porta da aplicação
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "app.jar"]