## Financial Bank App

**Utilizada por:** *Trabajadores entidad financiera*

### Requisitos
- Registro de Clientes
- Actualización de datos Cliente
- Eliminación de Clientes
- Creación de Productos Financieros para los Clientes
- Movimientos Transaccionales a los Productos Financieros
- Consultar Estado de Cuente Productos Financieros

### Entidades
##### Cliente
- `id`
- `idType`
- `numId`
- `name ->` Mayor a 2 caracteres
- `lastName ->` Mayor a 2 caracteres
- `email`
- `birthDate ->` Calcular si es menor de edad
- `createDate ->` Calculada automáticamente
- `lastModifiedDate ->` Calculada automáticamente
##### Producto (Cuentas)

>[!Important]
>- Al crearse es activa por defecto **sólo la cuenta de ahorro**
>- Sólo se puede cancelar si su saldo es 0

- `id`
- `productType ->` Cuenta Corriente o de Ahorros
- `productNumber ->` Único y generado automáticamente, con longitud de 10, de ahorros comienza con `53`, corriente en `33`
- `state ->` Activa, Inactiva, Cancelada
- `balance ->` Mayor o Igual a 0
- `gmfExempt`
- `createdAt`
- `modifiedAt`
- `user ->` *Foreign Key*

##### Transacciones (Movimientos Financieros)

>[!warning]
>Comprender bien

- `id`
- `typeTransaction ->` Consignación, Retiro y Transferencia entre Cuentas
- `amount`
- `originProduct ->` *Foreign Key*
- `destinyProduct ->` *Foreign Key*
- `transactionDate ->` Generada automáticamente
