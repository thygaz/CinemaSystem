# ETAPA 1: Imagem Base
# Começamos com uma imagem que já tem o Java 25 (OpenJDK) instalado em um sistema Linux.
# É a base da nossa "caixa".
FROM eclipse-temurin:25-jdk-jammy

# ETAPA 2: Diretório de Trabalho
# Cria uma pasta chamada '/app' dentro da imagem. É para lá que vamos copiar nosso projeto.
WORKDIR /app

# ETAPA 3: Copiar o Arquivo .jar
# Pega o arquivo .jar que o 'mvn package' gerou na pasta 'target' do seu PC
# e o copia para dentro da imagem, renomeando-o para 'app.jar'.
COPY target/*.jar app.jar

# ETAPA 4: Expor a Porta
# Avisa ao Docker que a aplicação dentro deste contêiner vai rodar na porta 8080.
# Isso não libera a porta para o seu PC ainda, apenas informa.
EXPOSE 8080

# ETAPA 5: Comando de Execução
# Define o comando que será executado assim que o contêiner for iniciado.
# É o mesmo que 'java -jar app.jar'.
ENTRYPOINT ["java", "-jar", "app.jar"]