package mteps.firmadigital.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DatosTokenResponse {

	public Datos datos;
    public boolean finalizado;
    public String mensaje;


    public static class Datos {
        @JsonProperty("data_token")
        public DataToken dataToken;
       
        public static class DataToken {
            public int certificates;
            public List<TokenData> data;
            @JsonProperty("private_keys") 
            public int privateKeys;

            public static class TokenData {
                public String tipo;
                @JsonProperty("tipo_desc")
                public String tipoDesc;
                public String alias;
                public String id;
                @JsonProperty("tiene_certificado")
                public boolean tieneCertificado;
                public String serialNumber;
                public boolean adsib;
                public String pem;
                public Validez validez;
                public Titular titular;
                public Emisor emisor;
                @JsonProperty("common_name")
                public String commonName;

            }

            public static class Validez {
                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                public Date desde;
                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                public Date hasta;

            }

            public static class Titular {
                @JsonProperty("dnQualifier")
                public String dnQualifier;
                public String uidNumber;
                public String UID;
                public String CN;
                public String T;
                public String O;
                public String OU;
                @JsonProperty("EmailAddress")
                public String emailAddress;
                public String description;

            }

            public static class Emisor {
                public String CN;
                public String O;

            }
        }
    }
}
