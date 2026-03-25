import { IsDateString, IsNumber, IsOptional, IsString, IsUUID, MaxLength, Min } from 'class-validator';

export class CreateCatchDto {
  @IsUUID()
  clientUuid!: string;

  @IsUUID()
  mangaId!: string;

  @IsUUID()
  participantId!: string;

  @IsNumber()
  @Min(0.001)
  weight!: number;

  @IsOptional()
  @IsNumber()
  @Min(0.1)
  length?: number;

  @IsDateString()
  capturedAt!: string;

  @IsOptional()
  @IsString()
  @MaxLength(500)
  notes?: string;
}
