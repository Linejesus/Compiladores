#include <stdio.h>

int main() {

int idade;
float altura;
char nome[100];
printf("Digite seu nome");
scanf("%s", nome);
printf("Digite sua idade");
scanf("%d", &idade);
printf("Digite sua altura");
scanf("%f", &altura);
printf("Nome informado");
printf("%s", nome);
printf("Idade informada");
printf("%d", idade);
printf("Altura informada");
printf("%f", altura);

return 0;
}
